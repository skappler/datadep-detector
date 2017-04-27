package org.dreads.instrumentation.disl.instr.guard;

import org.dreads.instrumentation.disl.analysis.BaseProgramScope;
import org.dreads.instrumentation.disl.instr.context.FieldStaticContext;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class GetGuard {
    @GuardMethod
    public static boolean isApplicable(MethodStaticContext msc, FieldStaticContext fsc) {
        return BaseProgramScope.isValid(msc.thisClassName()) && fsc.isValidField();
    }
}
