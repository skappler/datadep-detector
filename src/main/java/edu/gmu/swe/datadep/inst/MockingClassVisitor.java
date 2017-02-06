package edu.gmu.swe.datadep.inst;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import edu.gmu.swe.datadep.Instrumenter;
import edu.gmu.swe.datadep.MockedClass;

/**
 * This class adds the required interfaces
 * 
 * @author gambi
 *
 */
public class MockingClassVisitor extends ClassVisitor {
	boolean skipFrames = false;

	public MockingClassVisitor(ClassVisitor _cv, boolean skipFrames) {
		super(Opcodes.ASM5, _cv);
		this.skipFrames = skipFrames;
	}

	String className;
	boolean isClass = false;

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

		// System.out.println("MockingClassVisitor.visit() " + name);

		// make sure class is not private
		access = access & ~Opcodes.ACC_PRIVATE;
		access = access | Opcodes.ACC_PUBLIC;
		this.isClass = (access & Opcodes.ACC_INTERFACE) == 0;
		this.className = name;

		// If this class is mocked we also include another interface
		// TODO Removed to fix problem with missing deps
		if ( /* Instrumenter.isMockedClass(name) && */ isClass) {
			String[] iface = new String[interfaces.length + 1];
			System.arraycopy(interfaces, 0, iface, 0, interfaces.length);
			iface[interfaces.length] = Type.getInternalName(MockedClass.class);
			interfaces = iface;
			if (signature != null)
				signature = signature + Type.getDescriptor(MockedClass.class);
			// System.out.println("MockingClassVisitor.visit() Adding
			// MockedClass interface to " + name);
			// System.out.println("MockingClassVisitor.visit() new signature is
			// " + signature);
			// System.out.println("MockingClassVisitor.visit() new interfaces
			// are " + Arrays.toString(interfaces));
		}

		super.visit(version, access, name, signature, superName, interfaces);
	}

}
