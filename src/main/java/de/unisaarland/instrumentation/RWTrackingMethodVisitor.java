package de.unisaarland.instrumentation;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.tree.FieldNode;

import ch.usi.dag.disl.util.Logging;
import ch.usi.dag.util.logging.Logger;

/**
 * TODO Check this: Inserts instrumentation to record heap reads and writes/
 * 
 * @author jon
 *
 */
public class RWTrackingMethodVisitor extends AdviceAdapter implements Opcodes {

	private static final Logger __log = Logging.getPackageInstance();

	private boolean patchLDCClass;
	private String clazz;
	private boolean inUninitializedSuper = false;
	private boolean isStaticInitializer = false;
	private List<FieldNode> moreFields;

	public RWTrackingMethodVisitor(MethodVisitor mv, boolean patchLDCClass, String className, int acc,
			String methodName, String desc, List<FieldNode> moreFields) {
		super(Opcodes.ASM5, mv, acc, methodName, desc);
		this.patchLDCClass = patchLDCClass;
		this.clazz = className;
		this.inUninitializedSuper = "<init>".equals(methodName);
		this.isStaticInitializer = "<clinit>".equals(methodName);
		this.moreFields = moreFields;
		//
		__log.debug("RWTrackingMethodVisitor.RWTrackingMethodVisitor() " + methodName + " static init ? "
				+ isStaticInitializer);
	}

	/**
	 * Force the invocation of __initPrimDepInfo in the super class if there's
	 * any. This will trigger the initialization and the write()
	 */
	@Override
	protected void onMethodEnter() {
		__log.debug("RWTrackingMethodVisitor.onMethodEnter()");
		if (this.inUninitializedSuper) {
			this.inUninitializedSuper = false;
			super.visitVarInsn(ALOAD, 0);
			super.visitMethodInsn(INVOKEVIRTUAL, clazz, "__initPrimDepInfo", "()V", false);
		}
		if (this.isStaticInitializer) {

			System.out.println("RWTrackingMethodVisitor.onMethodEnter() Static Initializer for " + this.clazz);
			
			// Add the code before to invoke all the static field.. probably
			// this can be optimized otherwise the byte of this method grows too
			// much
			for (FieldNode fn : moreFields) {
				if ((fn.access & Opcodes.ACC_STATIC) != 0) {
					// Static 
					__log.debug(
							"RWTrackingMethodVisitor.onMethodEnter() Adding static initialization of DepInfo () for "
									+ fn.name + " in " + clazz);

					super.visitTypeInsn(Opcodes.NEW, Type.getInternalName(DependencyInfo.class));
					super.visitInsn(Opcodes.DUP);
					super.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(DependencyInfo.class), "<init>",
							"()V", false);
					// Problem is: if this is a primitive we cannot
					mv.visitFieldInsn(Opcodes.PUTSTATIC, clazz, fn.name, Type.getDescriptor(DependencyInfo.class));
				} else {
					__log.debug(
							"RWTrackingMethodVisitor.onMethodEnter() Skipping static initialization of DepInfo () for "
									+ fn.name + " in " + clazz);
				}
			}
		}
		this.inUninitializedSuper = false;
	}

	/// TODO No idea about this ...
	public void visitLdcInsn(Object cst) {
		__log.debug("RWTrackingMethodVisitor.visitLdcInsn() " + cst);

		if (cst instanceof Type && patchLDCClass) {
			super.visitLdcInsn(((Type) cst).getInternalName().replace("/", "."));
			super.visitInsn(Opcodes.ICONST_0);
			super.visitLdcInsn(clazz.replace("/", "."));
			super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Class", "forName",
					"(Ljava/lang/String;)Ljava/lang/Class;", false);
			super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getClassLoader",
					"()Ljava/lang/ClassLoader;", false);
			super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Class", "forName",
					"(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;", false);
		} else {
			super.visitLdcInsn(cst);
		}
	}

	int tmpLField = -1;
	int tmpDField = -1;

	// TODO This shall should be called only from DiSL Snippet right ?
	// Upon the "Load reference from array" try forces to invoke READ on what ?
	/*
	 * Visits a zero operand instruction.
	 * 
	 * @param opcode the opcode of the instruction to be visited. This opcode is
	 * either NOP, ACONST_NULL, ICONST_M1, ICONST_0, ICONST_1, ICONST_2,
	 * ICONST_3, ICONST_4, ICONST_5, LCONST_0, LCONST_1, FCONST_0, FCONST_1,
	 * FCONST_2, DCONST_0, DCONST_1, IALOAD, LALOAD, FALOAD, DALOAD, AALOAD,
	 * BALOAD, CALOAD, SALOAD, IASTORE, LASTORE, FASTORE, DASTORE, AASTORE,
	 * BASTORE, CASTORE, SASTORE, POP, POP2, DUP, DUP_X1, DUP_X2, DUP2, DUP2_X1,
	 * DUP2_X2, SWAP, IADD, LADD, FADD, DADD, ISUB, LSUB, FSUB, DSUB, IMUL,
	 * LMUL, FMUL, DMUL, IDIV, LDIV, FDIV, DDIV, IREM, LREM, FREM, DREM, INEG,
	 * LNEG, FNEG, DNEG, ISHL, LSHL, ISHR, LSHR, IUSHR, LUSHR, IAND, LAND, IOR,
	 * LOR, IXOR, LXOR, I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L,
	 * D2F, I2B, I2C, I2S, LCMP, FCMPL, FCMPG, DCMPL, DCMPG, IRETURN, LRETURN,
	 * FRETURN, DRETURN, ARETURN, RETURN, ARRAYLENGTH, ATHROW, MONITORENTER, or
	 * MONITOREXIT.
	 */
	// @Override
	// public void visitInsn(int opcode) {
	// __log.debug("RWTrackingMethodVisitor.visitInsn() - not implemented yet");
	// switch (opcode) {
	// // TODO What's this ? Whenever a class is loaded call a read on it?
	// or
	// // instantiated ?
	// case AALOAD: // Load reference from array
	// super.visitInsn(opcode);
	// super.visitInsn(DUP);
	// super.visitMethodInsn(INVOKESTATIC,
	// Type.getInternalName(DependencyInfo.class), "read",
	// "(Ljava/lang/Object;)V", false);
	// return;
	// }
	// super.visitInsn(opcode);
	// }

	// FIXME This must be called only by DiSL snippets. Note that primitives and
	// strings shall be handled in the owner class of the field.
	// Arrays also requires special dealing.
	/*
	 * Visits a field instruction. A field instruction is an instruction that
	 * loads or stores the value of a field of an object.
	 * 
	 * @param opcode the opcode of the type instruction to be visited. This
	 * opcode is either GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
	 */
	// @Override
	// public void visitFieldInsn(int opcode, String owner, String name, String
	// desc) {
	// __log.debug("RWTrackingMethodVisitor.visitFieldInsn() " + name + " not
	// implemented.");
	// if (Instrumenter.isIgnoredClass(owner)) {
	// super.visitFieldInsn(opcode, owner, name, desc);
	// return;
	// switch (opcode) {
	// case GETFIELD: // On FIELD read/access
	// super.visitInsn(DUP);
	// super.visitMethodInsn(INVOKESTATIC,
	// Type.getInternalName(DependencyInfo.class), "read",
	// "(Ljava/lang/Object;)V", false);
	//
	// Type t = Type.getType(desc);
	// switch (t.getSort()) {
	// case Type.ARRAY:
	// case Type.OBJECT:
	// // deal with String as well. Read from the declaring class the
	// // corresponding dep info data
	// if (desc.equals("Ljava/lang/String;")) {
	// super.visitInsn(DUP);
	// super.visitFieldInsn(opcode, owner, name + "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// super.visitMethodInsn(INVOKESTATIC,
	// Type.getInternalName(DependencyInfo.class), "read",
	// "(Ljava/lang/Object;)V", false);
	// super.visitFieldInsn(opcode, owner, name, desc);
	// } else {
	// // Note the order of visit ! We first call read to someone
	// // else and then we call it on our stuff
	// super.visitFieldInsn(opcode, owner, name, desc);
	// //
	// super.visitInsn(DUP);
	// super.visitMethodInsn(INVOKESTATIC,
	// Type.getInternalName(DependencyInfo.class), "read",
	// "(Ljava/lang/Object;)V", false);
	// }
	// break;
	// case Type.BOOLEAN:
	// case Type.BYTE:
	// case Type.CHAR:
	// case Type.INT:
	// case Type.FLOAT:
	// case Type.SHORT:
	// case Type.DOUBLE:
	// case Type.LONG:
	// super.visitInsn(DUP);
	// super.visitFieldInsn(opcode, owner, name + "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// super.visitMethodInsn(INVOKESTATIC,
	// Type.getInternalName(DependencyInfo.class), "read",
	// "(Ljava/lang/Object;)V", false);
	// super.visitFieldInsn(opcode, owner, name, desc);
	// break;
	// default:
	// throw new UnsupportedOperationException();
	// }
	//
	// break;
	// case GETSTATIC: // All static fields inside a class are managed via
	// an
	// // external DEP_INFO data
	// super.visitFieldInsn(opcode, owner, name, desc);
	// if (!Instrumenter.isIgnoredClass(owner)) {
	// super.visitFieldInsn(opcode, owner, name + "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// Label ok = new Label();
	// super.visitJumpInsn(IFNONNULL, ok);
	// super.visitTypeInsn(NEW, Type.getInternalName(DependencyInfo.class));
	// super.visitInsn(DUP);
	// super.visitMethodInsn(INVOKESPECIAL,
	// Type.getInternalName(DependencyInfo.class), "<init>", "()V",
	// false);
	// super.visitFieldInsn(PUTSTATIC, owner, name + "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// if (an != null) {
	// Object[] locals = removeLongsDoubleTopVal(an.locals);
	// Object[] stack = removeLongsDoubleTopVal(an.stack);
	// super.visitLabel(ok);
	// super.visitFrame(Opcodes.F_NEW, locals.length, locals, stack.length,
	// stack);
	// } else
	// super.visitLabel(ok);
	// super.visitFieldInsn(opcode, owner, name + "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// super.visitMethodInsn(INVOKEVIRTUAL,
	// Type.getInternalName(DependencyInfo.class), "read", "()V", false);
	// }
	// break;
	// case PUTSTATIC: // All static fields inside a class are managed via
	// an
	// // external DEP_INFO data
	// super.visitFieldInsn(opcode, owner, name, desc);
	// if (!Instrumenter.isIgnoredClass(owner)) {
	// super.visitFieldInsn(GETSTATIC, owner, name + "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// Label ok = new Label();
	// super.visitJumpInsn(IFNONNULL, ok);
	// super.visitTypeInsn(NEW, Type.getInternalName(DependencyInfo.class));
	// super.visitInsn(DUP);
	// // super.visitLdcInsn(Type.getObjectType(owner));
	// // super.visitLdcInsn(name);
	// // super.visitMethodInsn(INVOKESPECIAL,
	// // Type.getInternalName(DependencyInfo.class), "<init>",
	// // "(Ljava/lang/Class;Ljava/lang/String;)V", false);
	// super.visitMethodInsn(INVOKESPECIAL,
	// Type.getInternalName(DependencyInfo.class), "<init>", "()V",
	// false);
	// super.visitFieldInsn(PUTSTATIC, owner, name + "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// if (an != null) {
	// Object[] locals = removeLongsDoubleTopVal(an.locals);
	// Object[] stack = removeLongsDoubleTopVal(an.stack);
	// super.visitLabel(ok);
	// super.visitFrame(Opcodes.F_NEW, locals.length, locals, stack.length,
	// stack);
	// } else
	// super.visitLabel(ok);
	// super.visitFieldInsn(GETSTATIC, owner, name + "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// super.visitMethodInsn(INVOKEVIRTUAL,
	// Type.getInternalName(DependencyInfo.class), "write", "()V", false);
	// }
	// break;
	// case PUTFIELD:
	// t = Type.getType(desc);
	// if (inUninitializedSuper) {
	// super.visitFieldInsn(opcode, owner, name, desc);
	// return;
	// }
	// if (t.getSort() == Type.ARRAY || t.getSort() == Type.OBJECT) {
	//
	// // deal with String as well. Read from the declaring class the
	// // corresponding dep info data
	// //
	// if (desc.equals("Ljava/lang/String;")) {
	// // TODO t.getSize here should be 1
	// super.visitInsn(SWAP);
	// super.visitInsn(DUP);
	// super.visitFieldInsn(Opcodes.GETFIELD, owner, name +
	// "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// super.visitMethodInsn(INVOKESTATIC,
	// Type.getInternalName(DependencyInfo.class), "write",
	// "(Ljava/lang/Object;)V", false);
	// super.visitInsn(SWAP);
	// } else {
	// super.visitInsn(SWAP);
	// super.visitInsn(DUP);
	// super.visitMethodInsn(INVOKESTATIC,
	// Type.getInternalName(DependencyInfo.class), "write",
	// "(Ljava/lang/Object;)V", false);
	// super.visitInsn(SWAP);
	// }
	// //
	// super.visitFieldInsn(opcode, owner, name, desc);
	// } else {
	// switch (t.getSize()) {
	// case 1:
	// super.visitInsn(SWAP);
	// // super.visitInsn(DUP);
	// // super.visitMethodInsn(INVOKESTATIC,
	// // Type.getInternalName(DependencyInfo.class), "write",
	// // "(Ljava/lang/Object;)V", false);
	// super.visitInsn(DUP);
	// super.visitFieldInsn(Opcodes.GETFIELD, owner, name +
	// "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// super.visitMethodInsn(INVOKESTATIC,
	// Type.getInternalName(DependencyInfo.class), "write",
	// "(Ljava/lang/Object;)V", false);
	// super.visitInsn(SWAP);
	// super.visitFieldInsn(opcode, owner, name, desc);
	// break;
	// case 2:
	// super.visitInsn(DUP2_X1);
	// super.visitInsn(POP2);
	// super.visitInsn(DUP);
	// super.visitInsn(POP);
	// // super.visitMethodInsn(INVOKESTATIC,
	// // Type.getInternalName(DependencyInfo.class), "write",
	// // "(Ljava/lang/Object;)V", false);
	// super.visitInsn(DUP);
	// super.visitFieldInsn(Opcodes.GETFIELD, owner, name +
	// "__DEPENDENCY_INFO",
	// Type.getDescriptor(DependencyInfo.class));
	// super.visitMethodInsn(INVOKESTATIC,
	// Type.getInternalName(DependencyInfo.class), "write",
	// "(Ljava/lang/Object;)V", false);
	// super.visitInsn(DUP_X2);
	// super.visitInsn(POP);
	// super.visitFieldInsn(opcode, owner, name, desc);
	// break;
	// }
	// }
	// break;
	// }
	// }

	protected static Object[] removeLongsDoubleTopVal(List<Object> in) {
		ArrayList<Object> ret = new ArrayList<Object>();
		boolean lastWas2Word = false;
		for (Object n : in) {
			if (n == Opcodes.TOP && lastWas2Word) {
				// nop
			} else
				ret.add(n);
			if (n == Opcodes.DOUBLE || n == Opcodes.LONG)
				lastWas2Word = true;
			else
				lastWas2Word = false;
		}
		return ret.toArray();
	}

	LocalVariablesSorter lvs;

	public void setLVS(LocalVariablesSorter lvs) {
		this.lvs = lvs;
	}

	AnalyzerAdapter an;

	public void setAnalyzer(AnalyzerAdapter an) {
		this.an = an;
	}
}