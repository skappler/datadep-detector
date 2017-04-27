package org.dreads.instrumentation.disl.instr.context;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LineNumberNode;

import ch.usi.dag.disl.staticcontext.AbstractStaticContext;

public class LineNumberStaticContext extends AbstractStaticContext {
    public int getPrevLineNumber() {
        for(AbstractInsnNode ain = staticContextData.getRegionStart(); ain != null; ain = ain.getPrevious()) {
            if(ain.getType() == AbstractInsnNode.LINE) {
                return ((LineNumberNode)ain).line;
            }
        }
        return -1;
    }

    public int getNextLineNumber() {
        for(AbstractInsnNode ain = staticContextData.getRegionStart(); ain != null; ain = ain.getNext()) {
            if(ain.getType() == AbstractInsnNode.LINE) {
                return ((LineNumberNode)ain).line;
            }
        }
        return -1;
    }
}
