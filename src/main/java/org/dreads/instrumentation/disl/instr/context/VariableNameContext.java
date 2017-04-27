package org.dreads.instrumentation.disl.instr.context;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.VarInsnNode;

import ch.usi.dag.disl.staticcontext.AbstractStaticContext;

public class VariableNameContext extends AbstractStaticContext {

	public int getVariableIndex() {

		InsnList instructions = staticContextData.getMethodNode().instructions;
		AbstractInsnNode instr = staticContextData.getRegionStart();

		if (!(instr instanceof VarInsnNode)) {
			return -1;
		}

		if (!(instructions.contains(instr))) {
			return -1;
		}

		VarInsnNode vin = (VarInsnNode) instr;
		
		return vin.var;
	}
	
    public String getVarDesc() {
    	InsnList instructions = staticContextData.getMethodNode().instructions;
		AbstractInsnNode instr = staticContextData.getRegionStart();

		if (!(instr instanceof VarInsnNode)) {
			return "";
		}

		if (!(instructions.contains(instr))) {
			return "";
		}

		VarInsnNode vin = (VarInsnNode) instr;
		int index = vin.var;

		int offset = instructions.indexOf(vin);

		for (LocalVariableNode lvn : staticContextData.getMethodNode().localVariables) {

			if (!(instructions.contains(lvn.start) && instructions
					.contains(lvn.end))) {
				continue;
			}

			
//			int from = instructions.indexOf(lvn.start);
			int to = instructions.indexOf(lvn.end);
			//&& from < offset
			if (lvn.index == index && offset < to) {
				return lvn.desc; 
			}
		}

		return "";
    }

    public boolean isReferenceField() {
        return getVarDesc().startsWith("L");
    }

    public boolean isArray() {
        return getVarDesc().startsWith("[");
    }
	
    public boolean isPrimitive() {
        return !isReferenceField() && !isArray();        
    }
    
	public String getVariableName() {

		InsnList instructions = staticContextData.getMethodNode().instructions;
		AbstractInsnNode instr = staticContextData.getRegionStart();

		if (!(instr instanceof VarInsnNode)) {
			return "";
		}

		if (!(instructions.contains(instr))) {
			return "";
		}

		VarInsnNode vin = (VarInsnNode) instr;
		int index = vin.var;

		int offset = instructions.indexOf(vin);

		for (LocalVariableNode lvn : staticContextData.getMethodNode().localVariables) {

			if (!(instructions.contains(lvn.start) && instructions
					.contains(lvn.end))) {
				continue;
			}

			
//			int from = instructions.indexOf(lvn.start);
			int to = instructions.indexOf(lvn.end);
			//&& from < offset
			if (lvn.index == index && offset < to) {
				return lvn.name; 
			}
		}

		return "";
	}
}
