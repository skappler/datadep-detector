package de.unisaarland.instrumentation.disl.guard;

import ch.usi.dag.disl.annotation.GuardMethod;
import de.unisaarland.instrumentation.disl.context.FieldStaticContext;

public class MainGuard {

	@GuardMethod
	public static boolean isApplicable(FieldStaticContext fsc) {
		// This fails for Objects like Strings ...
		// return !Instrumenter.isIgnoredClass(fsc.getFieldOwner());
		return true;
	}
}
