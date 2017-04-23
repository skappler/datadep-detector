package de.unisaarland.instrumentation;

import java.util.LinkedList;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.tree.FieldNode;

import ch.usi.dag.disl.util.Logging;
import ch.usi.dag.util.logging.Logger;

public class DependencyTrackingClassVisitor extends ClassVisitor {
	boolean skipFrames = false;
	private static final Logger __log = Logging.getPackageInstance();

	String className;
	boolean isClass = false;

	private boolean patchLDCClass;
	private boolean addTaintField = true;

	// This contains the reference to the additional DependencyInfo fields that
	// we must include to deal with primitives and Strings fields
	LinkedList<FieldNode> moreFields = new LinkedList<FieldNode>();

	public DependencyTrackingClassVisitor(ClassVisitor _cv, boolean skipFrames) {
		super(Opcodes.ASM5, _cv);
		this.skipFrames = skipFrames;
	}

	/**
	 * Add the DependencyInstrumented interface to the class, unless the class
	 * is ignored.
	 */
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// make sure class is not private
		access = access & ~Opcodes.ACC_PRIVATE;
		access = access | Opcodes.ACC_PUBLIC;
		this.isClass = (access & Opcodes.ACC_INTERFACE) == 0;
		this.className = name;
		this.patchLDCClass = (version & 0xFFFF) < Opcodes.V1_5;
		if (!superName.equals("java/lang/Object") && !Instrumenter.isIgnoredClass(superName)) {
			addTaintField = false;
		}

		__log.debug("DependencyTrackingClassVisitor.visit() " + className);

		// Add interface
		if (!Instrumenter.isIgnoredClass(name) && isClass) {
			String[] iface = new String[interfaces.length + 1];
			System.arraycopy(interfaces, 0, iface, 0, interfaces.length);
			iface[interfaces.length] = Type.getInternalName(DependencyInstrumented.class);
			interfaces = iface;
			if (signature != null) {
				signature = signature + Type.getDescriptor(DependencyInstrumented.class);
			}
			__log.debug("Adding interface DependencyInstrumented to " + className);

		}
		super.visit(version, access, name, signature, superName, interfaces);
	}

	/**
	 * Add the FIELD__DEPENDENCY_INFO for fields that must be managed at the
	 * owner level, that is, primitives and String
	 */
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {

		__log.debug("DependencyTrackingClassVisitor.visitField() " + name + " inside " + className);
		Type t = Type.getType(desc);

		if ((access & Opcodes.ACC_STATIC) != 0 || (t.getSort() != Type.ARRAY && t.getSort() != Type.OBJECT)
				|| (desc.equals("Ljava/lang/String;"))) {
			moreFields.add(new FieldNode(access, name + "__DEPENDENCY_INFO", Type.getDescriptor(DependencyInfo.class),
					null, null));

			__log.debug("\t Adding " + name + "__DEPENDENCY_INFO to " + className);
		}

		return super.visitField(access, name, desc, signature, value);

	}

	// I suspect that this one is meant to add the tracking to objects created
	// and returned on the fly from this class which might not be a field to any
	// class
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		AnalyzerAdapter an = null;

		__log.debug("DependencyTrackingClassVisitor.visitMethod() " + name);

		if (!skipFrames) {
			an = new AnalyzerAdapter(className, access, name, desc, mv);
			mv = an;
		}

		// TODO Not sure what this actually does
		RWTrackingMethodVisitor rtmv = new RWTrackingMethodVisitor(mv, patchLDCClass, className, access, name, desc);
		
		mv = rtmv;
		if (!skipFrames) {
			rtmv.setAnalyzer(an);
		}
		LocalVariablesSorter lvs = new LocalVariablesSorter(access, desc, mv);
		rtmv.setLVS(lvs);
		return lvs;
	}

	/**
	 * Introduce the __DEPENDENCY_INFO field in this class and the
	 * implementation of the getDEPENDENCY_INFO() method from the
	 * DependencyInstrumented interface. If the DependencyInfo field is null
	 * then it also invokes DependencyInfo's constructor.
	 */
	@Override
	public void visitEnd() {
		__log.debug("DependencyTrackingClassVisitor.visitEnd() for " + className);

		// TODO What's this ? to propagate the instumentation ? Register the new
		// fields in the class (at the end of it)
		for (FieldNode fn : moreFields) {
			fn.accept(cv);
		}

		if (isClass) {

			// Add field to store dep info
			if (addTaintField) {
				super.visitField(Opcodes.ACC_PUBLIC, "__DEPENDENCY_INFO", Type.getDescriptor(DependencyInfo.class),
						null, null);
				__log.debug("visitEnd Adding __DEPENDENCY_INFO to " + className);

			}

			__log.debug("visitEnd Adding implementation of getDEPENDENCY_INFO() for " + className);

			// This is the implementation of getDEPENDENCY_INFO
			// TODO Add this as well
			MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC, "getDEPENDENCY_INFO",
					"()" + Type.getDescriptor(DependencyInfo.class), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitFieldInsn(Opcodes.GETFIELD, className, "__DEPENDENCY_INFO",
					Type.getDescriptor(DependencyInfo.class));
			mv.visitInsn(Opcodes.DUP);
			Label ok = new Label();
			mv.visitJumpInsn(Opcodes.IFNONNULL, ok);
			mv.visitInsn(Opcodes.POP);

			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(DependencyInfo.class));
			mv.visitInsn(Opcodes.DUP_X1);
			mv.visitInsn(Opcodes.DUP);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(DependencyInfo.class), "<init>", "()V",
					false);
			mv.visitFieldInsn(Opcodes.PUTFIELD, className, "__DEPENDENCY_INFO",
					Type.getDescriptor(DependencyInfo.class));
			mv.visitLabel(ok);
			mv.visitFrame(Opcodes.F_FULL, 1, new Object[] { className }, 1,
					new Object[] { Type.getInternalName(DependencyInfo.class) });
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();

			// TODO what does this do ? Introduce another method called
			// __initPrimDepInfo() which initialize the dep info of all the
			// static fields ?
			__log.debug("visitEnd Adding implementation of __initPrimDepInfo () for " + className);
			//
			mv = super.visitMethod(Opcodes.ACC_PUBLIC, "__initPrimDepInfo", "()V", null, null);
			mv.visitCode();

			// TODO Here this invokes write ? To what?
			for (FieldNode fn : moreFields) {
				__log.debug("Processing field " + fn.name);
				// For all the static fields in this class we explicitly
				// initialize the "external" DependencyInfo field
				// then, since we are initializing the fields we must call write
				// on them as well..
				if ((fn.access & Opcodes.ACC_STATIC) == 0) {
					__log.debug("visitEnd Adding initiatilization of static field " + fn.name);

					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(DependencyInfo.class));
					mv.visitInsn(Opcodes.DUP);
					mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(DependencyInfo.class), "<init>",
							"()V", false);

					// This propagates the write, but should be already there
					// using DiSL while instrumenting PUTFIELD ? TODO Check that
					// mv.visitInsn(Opcodes.DUP);
					// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
					// Type.getInternalName(DependencyInfo.class), "write",
					// "()V", false);
					mv.visitFieldInsn(Opcodes.PUTFIELD, className, fn.name, Type.getDescriptor(DependencyInfo.class));
				}
			}
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}
		super.visitEnd();
	}
}
