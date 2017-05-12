package edu.gmu.swe.datadep.inst;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

import edu.gmu.swe.datadep.DependencyInfo;
import edu.gmu.swe.datadep.Enumerations;
import edu.gmu.swe.datadep.Instrumenter;

/**
 * Inserts instrumentation to record heap reads and writes
 *
 * @author jon
 *
 */
public class RWTrackingMethodVisitor extends AdviceAdapter implements Opcodes {
	private boolean patchLDCClass;
	private String clazz;
	private boolean inUninitializedSuper = false;
	private boolean inStaticUninitializedSuper = false;
	private String methodName;

	public RWTrackingMethodVisitor(MethodVisitor mv, boolean patchLDCClass, String className, int acc,
			String methodName, String desc) {
		super(Opcodes.ASM5, mv, acc, methodName, desc);
		this.patchLDCClass = patchLDCClass;
		this.clazz = className;
		this.methodName = methodName;
		this.inUninitializedSuper = "<init>".equals(methodName);
		this.inStaticUninitializedSuper = "<clinit>".equals(methodName);
	}

	@Override
	protected void onMethodEnter() {
		if (this.inUninitializedSuper) {
			this.inUninitializedSuper = false;
			super.visitVarInsn(ALOAD, 0);
			super.visitMethodInsn(INVOKEVIRTUAL, clazz, "__initPrimDepInfo", "()V", false);
		}
		this.inUninitializedSuper = false;
		// TODO How to handle clinit ?
		//
	}

	public void visitLdcInsn(Object cst) {
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
		} else
			super.visitLdcInsn(cst);
	}

	int tmpLField = -1;
	int tmpDField = -1;

	@Override
	public void visitInsn(int opcode) {
		switch (opcode) {
		// TODO What's this ? Wheneve a class is loaded call a read on it? or
		// instantiated ?
		case AALOAD:
			super.visitInsn(opcode);
			super.visitInsn(DUP);
			super.visitMethodInsn(INVOKESTATIC, Type.getInternalName(DependencyInfo.class), "read",
					"(Ljava/lang/Object;)V", false);
			return;
		// case LASTORE:
		// if(tmpLField < 0)
		// tmpLField = lvs.newLocal(Type.LONG_TYPE);
		// super.visitVarInsn(LSTORE, tmpLField);
		// super.visitInsn(SWAP);
		// super.visitInsn(DUP);
		// super.visitMethodInsn(INVOKESTATIC,
		// Type.getInternalName(DependencyInfo.class), "write",
		// "(Ljava/lang/Object;)V", false);
		// super.visitInsn(SWAP);
		// super.visitVarInsn(LLOAD, tmpLField);
		// break;
		// case DASTORE:
		// if(tmpDField < 0)
		// tmpDField = lvs.newLocal(Type.DOUBLE_TYPE);
		// super.visitVarInsn(DSTORE, tmpDField);
		// super.visitInsn(SWAP);
		// super.visitInsn(DUP);
		// super.visitMethodInsn(INVOKESTATIC,
		// Type.getInternalName(DependencyInfo.class), "write",
		// "(Ljava/lang/Object;)V", false);
		// super.visitInsn(SWAP);
		// super.visitVarInsn(DLOAD, tmpDField);
		// break;
		// case AASTORE:
		// case IASTORE:
		// case SASTORE:
		// case CASTORE:
		// case BASTORE:
		// case FASTORE:
		// super.visitInsn(DUP2_X1);
		// super.visitInsn(POP2);
		// super.visitInsn(DUP);
		// super.visitMethodInsn(INVOKESTATIC,
		// Type.getInternalName(DependencyInfo.class), "write",
		// "(Ljava/lang/Object;)V", false);
		// super.visitInsn(DUP_X2);
		// super.visitInsn(POP);
		// break;
		}
		super.visitInsn(opcode);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		if (Instrumenter.isIgnoredClass(owner)) {
			super.visitFieldInsn(opcode, owner, name, desc);
			return;
		}
		switch (opcode) {
		case GETFIELD: // On FIELD read/access

			// Call Read to the object no matter what
			super.visitInsn(DUP);
			super.visitMethodInsn(INVOKESTATIC, Type.getInternalName(DependencyInfo.class), "read",
					"(Ljava/lang/Object;)V", false);

			Type t = Type.getType(desc);
			switch (t.getSort()) {
			case Type.ARRAY:
			case Type.OBJECT:
				// deal with String as well. Read from the declaring class the
				// corresponding dep info data
				if (desc.equals("Ljava/lang/String;") || // The PUTFIELD does
															// not use this one
															// ?!
						Enumerations.get().contains(t.getClassName().replaceAll("/", "."))) {
					super.visitInsn(DUP);
					super.visitFieldInsn(opcode, owner, name + "__DEPENDENCY_INFO",
							Type.getDescriptor(DependencyInfo.class));
					super.visitMethodInsn(INVOKESTATIC, Type.getInternalName(DependencyInfo.class), "read",
							"(Ljava/lang/Object;)V", false);
					super.visitFieldInsn(opcode, owner, name, desc);
				} else {
					// Note the order of visit ! We first call read to someone
					// else and then we call it on our stuff
					super.visitFieldInsn(opcode, owner, name, desc);
					// Why this one if we already have the one at the beginning
					// ~135
					// super.visitInsn(DUP);
					// super.visitMethodInsn(INVOKESTATIC,
					// Type.getInternalName(DependencyInfo.class), "read",
					// "(Ljava/lang/Object;)V", false);
				}
				break;
			case Type.BOOLEAN:
			case Type.BYTE:
			case Type.CHAR:
			case Type.INT:
			case Type.FLOAT:
			case Type.SHORT:
			case Type.DOUBLE:
			case Type.LONG:
				super.visitInsn(DUP);
				super.visitFieldInsn(opcode, owner, name + "__DEPENDENCY_INFO",
						Type.getDescriptor(DependencyInfo.class));
				super.visitMethodInsn(INVOKESTATIC, Type.getInternalName(DependencyInfo.class), "read",
						"(Ljava/lang/Object;)V", false);
				super.visitFieldInsn(opcode, owner, name, desc);
				break;
			default:
				throw new UnsupportedOperationException();
			}

			break;
		case GETSTATIC: // All static fields inside a class are managed via an
						// external DEP_INFO data
			super.visitFieldInsn(opcode, owner, name, desc);
			if (!Instrumenter.isIgnoredClass(owner)) {

				super.visitFieldInsn(opcode, owner, name + "__DEPENDENCY_INFO",
						Type.getDescriptor(DependencyInfo.class));
				Label ok = new Label();
				super.visitJumpInsn(IFNONNULL, ok);
				super.visitTypeInsn(NEW, Type.getInternalName(DependencyInfo.class));
				super.visitInsn(DUP);
				super.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(DependencyInfo.class), "<init>", "()V",
						false);
				super.visitFieldInsn(PUTSTATIC, owner, name + "__DEPENDENCY_INFO",
						Type.getDescriptor(DependencyInfo.class));
				if (an != null) {
					Object[] locals = removeLongsDoubleTopVal(an.locals);
					Object[] stack = removeLongsDoubleTopVal(an.stack);
					super.visitLabel(ok);
					super.visitFrame(Opcodes.F_NEW, locals.length, locals, stack.length, stack);
				} else
					super.visitLabel(ok);
				super.visitFieldInsn(opcode, owner, name + "__DEPENDENCY_INFO",
						Type.getDescriptor(DependencyInfo.class));
				super.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(DependencyInfo.class), "read", "()V", false);
			} else {
				System.out.println("GETSTATIC IGNORE " + name);
			}
			break;
		case PUTSTATIC: // All static fields inside a class are managed via an
						// external DEP_INFO data
			super.visitFieldInsn(opcode, owner, name, desc);

			if (!Instrumenter.isIgnoredClass(owner)) {
				super.visitFieldInsn(GETSTATIC, owner, name + "__DEPENDENCY_INFO",
						Type.getDescriptor(DependencyInfo.class));
				Label ok = new Label();
				super.visitJumpInsn(IFNONNULL, ok);
				super.visitTypeInsn(NEW, Type.getInternalName(DependencyInfo.class));
				super.visitInsn(DUP);
				// super.visitLdcInsn(Type.getObjectType(owner));
				// super.visitLdcInsn(name);
				// super.visitMethodInsn(INVOKESPECIAL,
				// Type.getInternalName(DependencyInfo.class), "<init>",
				// "(Ljava/lang/Class;Ljava/lang/String;)V", false);
				super.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(DependencyInfo.class), "<init>", "()V",
						false);
				super.visitFieldInsn(PUTSTATIC, owner, name + "__DEPENDENCY_INFO",
						Type.getDescriptor(DependencyInfo.class));
				if (an != null) {
					Object[] locals = removeLongsDoubleTopVal(an.locals);
					Object[] stack = removeLongsDoubleTopVal(an.stack);
					super.visitLabel(ok);
					super.visitFrame(Opcodes.F_NEW, locals.length, locals, stack.length, stack);
				} else {
					super.visitLabel(ok);
				}

				super.visitFieldInsn(GETSTATIC, owner, name + "__DEPENDENCY_INFO",
						Type.getDescriptor(DependencyInfo.class));
				super.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(DependencyInfo.class), "write", "()V", false);

			} else {
				System.out.println("PUTSTATIC IGNORE " + name);
			}
			break;
		case PUTFIELD:
			t = Type.getType(desc);
			if (inUninitializedSuper) {
				// At this point __initPrimDepInfo() is already called
				super.visitFieldInsn(opcode, owner, name, desc);
				return;
			}
			if (t.getSort() == Type.ARRAY || t.getSort() == Type.OBJECT) {

				// deal with String as well. Read from the declaring class the
				// corresponding dep info data
				//
				// Enums and such might be here, but other might be here as well
				// ?!!
				// if ( Instrumenter.isIgnoredClass( t.getClassName() ) ) {

				// If the following code does not run, it works for String ? For
				// Enums is ok, but results in missing the write inside the
				// owner ? In particular this c
				if (Enumerations.get().contains(t.getClassName().replaceAll("/", "."))
						|| String.class.getName().equals(t.getClassName().replaceAll("/", ".")) //
				) {

					// System.out.println("RWTrackingMethodVisitor.visitFieldInsn()
					// Patching PUTFIELD for field " + owner
					// + "." + name + " inside " + this.clazz + "." +
					// this.methodName);

					// TODO t.getSize here should be 1
					// On the stack now we have value
					// VALUE is on > value, objectref

					//
					// mv.visitFieldInsn(GETFIELD, "crystal/model/DataSource",
					// "_repoKind", "Lcrystal/model/DataSource$RepoKind;");

					// super.visitInsn(DUP); // > value, value, objectref
					super.visitVarInsn(ALOAD, 0);
					super.visitFieldInsn(GETFIELD, owner, name, desc);
					//
					Label l1 = new Label();
					// // Check if null -> pop value
					super.visitJumpInsn(IFNULL, l1); // > value, objectref
					Label l2 = new Label();
					super.visitLabel(l2);

					// // IF BRANCH == NOT NULL
					super.visitInsn(SWAP); //
					super.visitInsn(DUP);
					super.visitFieldInsn(Opcodes.GETFIELD, owner, name + "__DEPENDENCY_INFO",
							Type.getDescriptor(DependencyInfo.class));
					super.visitMethodInsn(INVOKESTATIC, Type.getInternalName(DependencyInfo.class), "write",
							"(Ljava/lang/Object;)V", false);
					super.visitInsn(SWAP);
					// // ELSE BRANCH --> OBJECT IS NULL ?!
					// // Label l4 = new Label();
					// // super.visitJumpInsn(GOTO, l4);
					super.visitLabel(l1);
					// /// DO NOTHING
					// /// LOG THIS NOT NULL
					// super.visitLabel(l4);

				} else {

					super.visitInsn(SWAP);
					super.visitInsn(DUP);
					super.visitMethodInsn(INVOKESTATIC, Type.getInternalName(DependencyInfo.class), "write",
							"(Ljava/lang/Object;)V", false);
					super.visitInsn(SWAP);
				}
				// THIS IS THE ACTUAL PUT FIELD value, objref -> pop pop
				super.visitFieldInsn(opcode, owner, name, desc);

				// //
				// if (name.equals("_remoteCmd")) {
				// super.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
				// "Ljava/io/PrintStream;");
				// super.visitLdcInsn("AFTER PUFIELD " + this.clazz + "." + name
				// + " >> is --- ");
				// super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
				// "print", "(Ljava/lang/String;)V",
				// false);
				// super.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
				// "Ljava/io/PrintStream;");
				// //
				// super.visitVarInsn(ALOAD, 0);
				// super.visitFieldInsn(GETFIELD, owner, name, desc);
				// super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
				// "println", "(Ljava/lang/Object;)V",
				// false);
				// }
			} else {
				switch (t.getSize()) {
				case 1:
					super.visitInsn(SWAP);
					super.visitInsn(DUP);
					super.visitFieldInsn(Opcodes.GETFIELD, owner, name + "__DEPENDENCY_INFO",
							Type.getDescriptor(DependencyInfo.class));
					super.visitMethodInsn(INVOKESTATIC, Type.getInternalName(DependencyInfo.class), "write",
							"(Ljava/lang/Object;)V", false);
					super.visitInsn(SWAP);
					super.visitFieldInsn(opcode, owner, name, desc);
					break;
				case 2:
					super.visitInsn(DUP2_X1);
					super.visitInsn(POP2);
					super.visitInsn(DUP);
					super.visitInsn(POP);
					// super.visitMethodInsn(INVOKESTATIC,
					// Type.getInternalName(DependencyInfo.class), "write",
					// "(Ljava/lang/Object;)V", false);
					super.visitInsn(DUP);
					super.visitFieldInsn(Opcodes.GETFIELD, owner, name + "__DEPENDENCY_INFO",
							Type.getDescriptor(DependencyInfo.class));
					super.visitMethodInsn(INVOKESTATIC, Type.getInternalName(DependencyInfo.class), "write",
							"(Ljava/lang/Object;)V", false);
					super.visitInsn(DUP_X2);
					super.visitInsn(POP);
					super.visitFieldInsn(opcode, owner, name, desc);
					break;
				}
			}
			break;
		}
	}

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