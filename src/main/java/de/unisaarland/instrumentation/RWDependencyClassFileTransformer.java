package de.unisaarland.instrumentation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.SerialVersionUIDAdder;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.util.Logging;
import ch.usi.dag.util.logging.Logger;

public class RWDependencyClassFileTransformer implements ch.usi.dag.disl.Transformer {

	private static final Logger __log = Logging.getPackageInstance();

	// How to log ?
	public RWDependencyClassFileTransformer() {
		__log.debug("RWDependencyClassFileTransformer.RWDependencyClassFileTransformer()");
	}

	/**
	 * TODO Do we need to prevent explicitly to instrument our own classes ?
	 * Somehow this get stuck when calling Instrumenter... suspect a cycle ?
	 */
	@Override
	public byte[] transform(byte[] classfileBuffer) throws Exception {

		ClassReader cr = new ClassReader(classfileBuffer);
		String className = cr.getClassName();
		__log.debug("Processing " + className);

		if (Instrumenter.isIgnoredClass(className)) {
			__log.debug("Ignoring " + className);
			return classfileBuffer;
		}

		ClassNode cn = new ClassNode();

		cr.accept(cn, ClassReader.SKIP_CODE);
		boolean skipFrames = false;
		if (cn.version >= 100 || cn.version < 50) {
			skipFrames = true;
		}

		// Skip Interfaces
		if ((cn.access & Opcodes.ACC_INTERFACE) != 0) {
			__log.debug("Ignoring Interface " + className);
			return classfileBuffer;
		}

		// TODO Apply Mocked class patch later
		// /*
		// * When a class is mocked we do not care about it, but, because it
		// might
		// * be a subclass of an instrumented class we have to tag it with an
		// * additional interface. In any case, we return at this point to avoid
		// * re-applying the RW machinery
		// */
		// TraceClassVisitor cv = null;
		// //
		// if (cn.interfaces != null) {
		// for (Object s : cn.interfaces) {
		// if (Type.getInternalName(MockedClass.class).equals(s)) {
		// System.out.println(
		// "RWDependencyClassFileTransformer.transform() " + className + "
		// already a MockedClass");
		// return classfileBuffer;
		// }
		// }
		// }
		// if (Instrumenter.isMockedClass(cn.name)) {
		// try {
		// ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
		//
		// cr.accept(
		// // new CheckClassAdapter(
		// new SerialVersionUIDAdder(new MockingClassVisitor(cw, skipFrames))
		// // )
		// , ClassReader.EXPAND_FRAMES);
		//
		// return cw.toByteArray();
		// } catch (Throwable ex) {
		// ex.printStackTrace();
		// cv = new TraceClassVisitor(null, null);
		// try {
		// cr.accept(new CheckClassAdapter(new SerialVersionUIDAdder(new
		// MockingClassVisitor(cv, skipFrames))),
		// ClassReader.EXPAND_FRAMES);
		// } catch (Throwable ex2) {
		// ex2.printStackTrace();
		// }
		// System.exit(-1);
		// return new byte[0];
		//
		// }
		// }

		/*
		 * If we have already instrumented this class we stop here and return
		 * its bytecode
		 */
		if (cn.interfaces != null) {
			for (Object s : cn.interfaces) {
				if (Type.getInternalName(DependencyInstrumented.class).equals(s)) {
					__log.debug("Ignoring " + className);
					return classfileBuffer;
				}
			}
		}

		/*
		 * Since the interface might be applied to a super (possibly abstract)
		 * class of this class, we need to check if the tainting interface
		 * method is already here (ok) or not (we need to add it).
		 */
		for (Object mn : cn.methods) {
			if (((MethodNode) mn).name.equals("getDEPENDENCY_INFO")) {
				__log.debug("Ignoring " + className);
				return classfileBuffer;
			}
		}

		/*
		 * At this point we are sure that the taint interface was not yet
		 * applied to this class
		 */
		try {
			__log.debug("Transforming " + className);
			// TODO Shall we prevent this transformation for any reason? For
			// example, this is a class which belongs to the instrumentation as
			// well or DiSL will take care of it ?
			// ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
			// This made it ?!
			ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

			cr.accept(new SerialVersionUIDAdder(new DependencyTrackingClassVisitor(cw, skipFrames)),
					ClassReader.EXPAND_FRAMES);

			__log.debug("Done with " + className + " bytes " + cw.toByteArray().length + " unmarshall ");

			// This fails with FULL
			// ClassNodeHelper.FULL.unmarshal(cw.toByteArray());

			return cw.toByteArray();
		} catch (Throwable ex) {
			__log.error("Problems transforming " + className + "  " + ex.getMessage());
			ex.printStackTrace();
			// TODO I have not idea what thos methods are for, probably
			// debugging
			// cv = new TraceClassVisitor(null, null);
			// try {
			// cr.accept(
			// new CheckClassAdapter(
			// new SerialVersionUIDAdder(new DependencyTrackingClassVisitor(cv,
			// skipFrames))),
			// ClassReader.EXPAND_FRAMES);
			// } catch (Throwable ex2) {
			// ex2.printStackTrace();
			// }
			// System.err.println("method so far:");
			// if (!innerException) {
			// PrintWriter pw = null;
			// try {
			// pw = new PrintWriter(new FileWriter("lastClass.txt"));
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// cv.p.print(pw);
			// pw.flush();
			// }
			// System.out.println("Saving " + className);
			// File f = new File("debug/" + className.replace("/", ".") +
			// ".class");
			// try {
			// FileOutputStream fos = new FileOutputStream(f);
			// fos.write(classfileBuffer);
			// fos.close();
			// } catch (Exception ex2) {
			// ex.printStackTrace();
			// }
			// Is this safe ?!
			// System.exit(-1);
			// return new byte[0];
			return null;

		}
	}

}
