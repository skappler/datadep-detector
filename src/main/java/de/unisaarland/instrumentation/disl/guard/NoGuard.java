package de.unisaarland.instrumentation.disl.guard;

import ch.usi.dag.disl.annotation.GuardMethod;
import de.unisaarland.instrumentation.disl.context.FieldStaticContext;

public class NoGuard {

	@GuardMethod
	public static boolean isApplicable(FieldStaticContext fsc) {
		return false;
	}
}
