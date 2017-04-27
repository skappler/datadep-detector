package org.dreads.instrumentation.disl.instr.guard;

import org.dreads.instrumentation.disl.analysis.CollectionScope;
import org.dreads.instrumentation.disl.instr.context.MethodCallStaticContext;

import ch.usi.dag.disl.annotation.GuardMethod;

public class CallGuard {
    @GuardMethod
    public static boolean isApplicable(MethodCallStaticContext mcsc) {
        return CollectionScope.isValid(mcsc.getOwner());
    }
    
    
}
