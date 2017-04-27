package org.dreads.instrumentation.disl.instr.context;

import org.dreads.instrumentation.disl.analysis.BaseProgramScope;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class FieldStaticContext extends MethodStaticContext {
    public String getFieldOwner() {
        return ((FieldInsnNode)staticContextData.getRegionStart()).owner;
    }

    public String getFieldName() {
        return ((FieldInsnNode)staticContextData.getRegionStart()).name;
    }

    public String getFieldDesc() {
        return ((FieldInsnNode)staticContextData.getRegionStart()).desc;
    }

    public boolean isReferenceField() {
        return getFieldDesc().startsWith("L");
    }

    public boolean isArray() {
        return getFieldDesc().startsWith("[");
    }

    public boolean isPrimitive() {
        return !isReferenceField() && !isArray();        
    }

    public boolean isValidField() {
        return BaseProgramScope.isValid(((FieldInsnNode)staticContextData.getRegionStart()).owner);
    }

    public boolean isValidLocation() {
        if(!thisMethodName().equals("<init>")) {
            return true;
        }

        for(AbstractInsnNode ain = staticContextData.getRegionStart(); ain != null; ain = ain.getPrevious()) {
            if(ain.getOpcode() == Opcodes.INVOKESPECIAL) {
                return true;
            }
        }
        return false;
    }
}
