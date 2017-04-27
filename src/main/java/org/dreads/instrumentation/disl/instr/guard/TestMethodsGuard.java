package org.dreads.instrumentation.disl.instr.guard;

import org.dreads.instrumentation.disl.analysis.BaseProgramScope;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class TestMethodsGuard {
	@GuardMethod
	public static boolean isApplicable(MethodStaticContext msc) {
		/*
		 * )&&(className.toLowerCase().endsWith("test")||className.toLowerCase()
		 * .contains("evosuite")||className.toLowerCase().contains("dreadstest")
		 * ||className.toLowerCase().contains("btest")||className.toLowerCase().
		 * endsWith("tests")||className.toLowerCase().contains("testsuite")||
		 * className.toLowerCase().contains("/test")||className.toLowerCase().
		 * contains("randoop")) );
		 * 
		 * Basically valid test is by the name of the class.
		 */
		return BaseProgramScope.isValidTest(msc.thisClassName()) && !msc.thisMethodName().equals("<init>");
	}
}
