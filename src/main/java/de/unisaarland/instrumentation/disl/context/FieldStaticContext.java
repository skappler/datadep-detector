package de.unisaarland.instrumentation.disl.context;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class FieldStaticContext extends MethodStaticContext {

	//

	public String getFieldOwner() {
		// System.out.println("FieldStaticContext.getFieldOwner() " +
		// staticContextData);
		// System.out.println("FieldStaticContext.getFieldOwner() " +
		// staticContextData.getRegionStart());
		return ((FieldInsnNode) staticContextData.getRegionStart()).owner;
	}

	public String getFieldName() {

		// System.out.println("FieldStaticContext.getFieldName() " +
		// staticContextData);
		// System.out.println("FieldStaticContext.getFieldName() " +
		// staticContextData.getRegionStart());

		// For some reason

		return ((FieldInsnNode) staticContextData.getRegionStart()).name;
	}

	public String getFieldDesc() {
		// System.out.println("FieldStaticContext.getFieldDesc() " +
		// staticContextData);
		// System.out.println("FieldStaticContext.getFieldDesc() " +
		// staticContextData.getRegionStart());
		return ((FieldInsnNode) staticContextData.getRegionStart()).desc;
	}

	public boolean isReferenceField() {
		if (getFieldDesc() == null) {
			System.out.println("FieldStaticContext.isReferenceField() Null Desc");
			return false;
		}
		return getFieldDesc().startsWith("L");
	}

	public boolean isArray() {
		if (getFieldDesc() == null) {
			// Not sure how to deal with this case
			System.out.println("FieldStaticContext.isArray() Null Desc");
			return false;
		}
		return getFieldDesc().startsWith("[");
	}

	public boolean isBoxedType() {
		String desc = getFieldDesc();
		if (desc == null) {
			System.out.println("FieldStaticContext.isBoxedType() Null Desc");
			return false;
		}
		return desc.equals("Ljava/lang/Boolean;") || //
				desc.equals("Ljava/lang/Character;") || //
				desc.equals("Ljava/lang/Float;") || //
				desc.equals("Ljava/lang/Byte;") || //
				desc.equals("Ljava/lang/Short;") || //
				desc.equals("Ljava/lang/Integer;") || //
				desc.equals("Ljava/lang/Double;") || //
				desc.equals("Ljava/lang/Long;")//
		;
	}

	// Probably here String as well...
	// Consider Long/Boolean and such as primitives?
	public boolean isPrimitive() {
		return (!isReferenceField() && !isArray()) || isBoxedType();
	}

	// TODO What about this ?
	// public boolean isValidField() {
	// return BaseProgramScope.isValid(((FieldInsnNode)
	// staticContextData.getRegionStart()).owner);
	// }

	public boolean isValidLocation() {
		if (!thisMethodName().equals("<init>")) {
			return true;
		}

		for (AbstractInsnNode ain = staticContextData.getRegionStart(); ain != null; ain = ain.getPrevious()) {
			if (ain.getOpcode() == Opcodes.INVOKESPECIAL) {
				return true;
			}
		}
		return false;
	}
}
