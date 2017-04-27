package org.dreads.instrumentation.disl.instr.guard;

import org.dreads.instrumentation.disl.analysis.BaseProgramScope;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class MethodsGuard {
	 @GuardMethod
	    public static boolean isApplicable(MethodStaticContext msc) {
	        return BaseProgramScope.isValid(msc.thisClassName()) ; //&& !msc.thisMethodName().equals("<init>");
	    }
}
