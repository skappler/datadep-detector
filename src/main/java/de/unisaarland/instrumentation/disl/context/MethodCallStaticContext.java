package de.unisaarland.instrumentation.disl.context;

import org.objectweb.asm.tree.MethodInsnNode;

import ch.usi.dag.disl.staticcontext.AbstractStaticContext;

public class MethodCallStaticContext extends AbstractStaticContext {
	public String getDesc() {
		return ((MethodInsnNode) staticContextData.getRegionStart()).desc;
	}

	public String getName() {
		return ((MethodInsnNode) staticContextData.getRegionStart()).name;
	}

	public String getOwner() {
		return ((MethodInsnNode) staticContextData.getRegionStart()).owner;
	}

	public String getTarget() {
		return getOwner() + "." + getName() + getDesc();
	}
}
