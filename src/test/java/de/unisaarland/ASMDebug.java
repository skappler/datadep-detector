package de.unisaarland;

import de.unisaarland.instrumentation.DependencyInfo;

public class ASMDebug {

	private DependencyInfo __DEPENDENCY_INFO;

	public DependencyInfo getDEPENDENCY_INFO() {
		if (__DEPENDENCY_INFO == null) {
			__DEPENDENCY_INFO = new DependencyInfo();
		}
		return __DEPENDENCY_INFO;
	}
}
