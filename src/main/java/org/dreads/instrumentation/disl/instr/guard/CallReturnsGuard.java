package org.dreads.instrumentation.disl.instr.guard;

import org.dreads.instrumentation.disl.analysis.CollectionScope;
import org.dreads.instrumentation.disl.instr.context.MethodCallStaticContext;

import ch.usi.dag.disl.annotation.GuardMethod;

public class CallReturnsGuard {
    @GuardMethod
    public static boolean isApplicable(MethodCallStaticContext mcsc) {
        String desc = mcsc.getDesc();
        return CollectionScope.isValid(mcsc.getOwner()) && (desc.charAt(desc.indexOf(')') + 1) != 'V');
    }
}
