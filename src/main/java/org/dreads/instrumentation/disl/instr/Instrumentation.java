package org.dreads.instrumentation.disl.instr;


import org.dreads.analysis.config.Properties;
import org.dreads.instrumentation.disl.analysis.EventHandler;
import org.dreads.instrumentation.disl.instr.context.FieldStaticContext;
import org.dreads.instrumentation.disl.instr.context.LineNumberStaticContext;
import org.dreads.instrumentation.disl.instr.context.MethodCallStaticContext;
import org.dreads.instrumentation.disl.instr.context.VariableNameContext;
import org.dreads.instrumentation.disl.instr.guard.BBOutGuard;
import org.dreads.instrumentation.disl.instr.guard.CallGuard;
import org.dreads.instrumentation.disl.instr.guard.CallReturnsGuard;
import org.dreads.instrumentation.disl.instr.guard.GetGuard;
import org.dreads.instrumentation.disl.instr.guard.InterfaceGuard;
import org.dreads.instrumentation.disl.instr.guard.LoadGuard;
import org.dreads.instrumentation.disl.instr.guard.MethodsGuard;
import org.dreads.instrumentation.disl.instr.guard.MethodsGuardAfter;
import org.dreads.instrumentation.disl.instr.guard.PutGuard;
import org.dreads.instrumentation.disl.instr.guard.StoreGuard;
import org.dreads.instrumentation.disl.instr.guard.TestMethodsGuard;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BasicBlockMarker;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.marker.InsnNodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.staticcontext.BasicBlockStaticContext;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class Instrumentation {
    @SyntheticLocal static Object retVal;
    @SyntheticLocal static Object owner;
    @SyntheticLocal static Object oldVal;
    @SyntheticLocal static Object newVal;
    @SyntheticLocal static Object arrayRef;
    @SyntheticLocal static int index;

       
    /** TEST CASES INSTRUMENTATION **/
    @Before(marker = InsnNodeMarker.class, args = "org.objectweb.asm.tree.LineNumberNode", guard = TestMethodsGuard.class, scope = Properties.SCOPE, order = 1)
    public static void beforeEachLine(LineNumberStaticContext lnsc, MethodStaticContext msc) {
    	EventHandler.instanceOf().beforeEachLine(msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(), lnsc.getPrevLineNumber());
    }
    
    @After(marker = BodyMarker.class, guard = TestMethodsGuard.class, scope = Properties.SCOPE, order = 0)
    public static void onTestMethodCompletion(MethodStaticContext msc) {
    	EventHandler.instanceOf().onTestMethodCompletion(msc.thisMethodFullName());
    }

    @Before(marker = BodyMarker.class, guard = TestMethodsGuard.class, scope = Properties.SCOPE, order = 0)
    public static void onTestEntry(MethodStaticContext msc) {
    	EventHandler.instanceOf().onTestMethodEntry(msc.thisMethodFullName());
    }

    @AfterReturning(marker = BytecodeMarker.class, args = "astore", guard = TestMethodsGuard.class, scope = Properties.SCOPE, order = 10)
    public static void afterTestVariable(VariableNameContext vnc, DynamicContext dc) {
    	EventHandler.instanceOf().onVariablePut(System.identityHashCode(dc.getThis()), vnc.getVariableName(), System.identityHashCode(dc.getLocalVariableValue(vnc.getVariableIndex(), Object.class)));
    }
       
    /** METHOD BODIES **/
    @Before(marker = BodyMarker.class, guard = MethodsGuardAfter.class, scope = Properties.SCOPE, order = 1000)
    public static void onMethodEntry(MethodStaticContext msc, DynamicContext dc) {
        EventHandler.instanceOf().onMethodEntry(
            msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(),
            dc.getThis()
        );
    }

    @After(marker = BodyMarker.class, guard = MethodsGuard.class, scope = Properties.SCOPE, order = 1000)
    public static void onMethodCompletion(MethodStaticContext msc, DynamicContext dc) {
        EventHandler.instanceOf().onMethodCompletion(
            msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(), dc.getThis()
        );
      
    }
    
//    @AfterReturning(marker = BodyMarker.class, guard = MethodsGuardAfter.class, scope = Properties.SCOPE, order = 1000)
//    public static void onInitCompletion(MethodStaticContext msc, DynamicContext dc) {
//        EventHandler.instanceOf().onMethodCompletion(
//            msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(), dc.getThis()
//        );
//     
//    }
    
    @Before(marker = BasicBlockMarker.class, guard = InterfaceGuard.class, scope = Properties.SCOPE, order = 1000)
    public static void onBasicBlockEntry(BasicBlockStaticContext bbsc, MethodStaticContext msc, DynamicContext dc) {
    	EventHandler.instanceOf().onBasicBlockIn(bbsc.getBBindex(), bbsc.getBBentryLine(), bbsc.getBBexitLine(),msc.thisClassCanonicalName(), msc.thisMethodName() + msc.thisMethodDescriptor(), msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(), dc.getThis());
    }
    
    @AfterReturning(marker = BasicBlockMarker.class, guard = BBOutGuard.class, scope = Properties.SCOPE, order = 1000)
    public static void onBasicBlockExit(BasicBlockStaticContext bbsc, MethodStaticContext msc, DynamicContext dc) {	
    	EventHandler.instanceOf().onBasicBlockOut(bbsc.getBBindex(), bbsc.getBBentryLine(), bbsc.getBBexitLine(), msc.thisClassCanonicalName(), msc.thisMethodName() + msc.thisMethodDescriptor(), msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(), dc.getThis());
    }
 
    /** METHOD CALLS **/
    @AfterReturning(marker = BytecodeMarker.class, args = "invokevirtual, invokestatic, invokeinterface", guard = CallReturnsGuard.class, scope = Properties.SCOPE, order = 10)
    public static void afterMethodCall1(DynamicContext dc) {
        retVal = dc.getStackValueXXX(0, Object.class);
    }

    @AfterReturning(marker = BytecodeMarker.class, args = "invokevirtual, invokestatic, invokeinterface", guard = CallGuard.class, scope = Properties.SCOPE, order = 100)
    public static void afterMethodCall2(LineNumberStaticContext lnsc, MethodCallStaticContext mcsc, MethodStaticContext msc, DynamicContext dc, ArgumentProcessorContext apc) {
        EventHandler.instanceOf().afterMethodCall(
            lnsc.getPrevLineNumber(),
            msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(),
            mcsc.getTarget(),
            dc.getThis(),
            apc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS),
            apc.getArgs(ArgumentProcessorMode.CALLSITE_ARGS),
            retVal
        );
        retVal = null;
    }

    /** GETFIELD **/
    @Before(marker = BytecodeMarker.class, args = "getfield", guard = GetGuard.class, scope = Properties.SCOPE, order = 100)
    public static void beforeGetField(FieldStaticContext fsc, DynamicContext dc) {
        owner = dc.getStackValue(0, Object.class);
    }

    @AfterReturning(marker = BytecodeMarker.class, args = "getfield", guard = GetGuard.class, scope = Properties.SCOPE, order = 100)
    public static void afterRetGetField(LineNumberStaticContext lnsc, BasicBlockStaticContext bbsc, FieldStaticContext fsc, MethodStaticContext msc, DynamicContext dc) {
        EventHandler.instanceOf().onInstanceFieldGet(
            lnsc.getPrevLineNumber(),
        	msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(),
        	bbsc.getBBindex(),
        	dc.getThis(),
            fsc.getFieldOwner(),
            fsc.getFieldDesc(),
            fsc.getFieldName(),
            owner,
            dc.getStackValueXXX(0, Object.class),
            fsc.isArray(),
            fsc.isPrimitive()
        );
    }

    /** GETSTATIC **/
    @AfterReturning(marker = BytecodeMarker.class, args = "getstatic", guard = GetGuard.class, scope = Properties.SCOPE, order = 100)
    public static void afterRetGetStatic(LineNumberStaticContext lnsc, FieldStaticContext fsc, MethodStaticContext msc, ClassContext cc, DynamicContext dc) {
    	EventHandler.instanceOf().onStaticFieldGet(
            lnsc.getPrevLineNumber(),
            msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(),
            dc.getThis(),
            fsc.getFieldOwner(),
            fsc.getFieldDesc(),
            fsc.getFieldName(),
            cc.asClass(fsc.getFieldOwner()),
            dc.getStackValueXXX(0, Object.class),
            fsc.isArray(),
            fsc.isPrimitive()
        );
    }

    /** PUTFIELD **/
    @Before(marker = BytecodeMarker.class, args = "putfield", guard = PutGuard.class, scope = Properties.SCOPE, order = 100)
    public static void beforePutField(LineNumberStaticContext lnsc, FieldStaticContext fsc, MethodStaticContext msc, DynamicContext dc) {
        owner = dc.getStackValue(1, Object.class);
        oldVal = dc.getInstanceFieldValueXXX(owner, fsc.getFieldOwner(), fsc.getFieldName(), fsc.getFieldDesc(), Object.class);
        newVal = dc.getStackValueXXX(0, Object.class);
    }

    @AfterReturning(marker = BytecodeMarker.class, args = "putfield", guard = PutGuard.class, scope = Properties.SCOPE, order = 100)
    public static void afterRetPutField(LineNumberStaticContext lnsc, FieldStaticContext fsc, MethodStaticContext msc, DynamicContext dc) {
        EventHandler.instanceOf().onInstanceFieldPut(
                lnsc.getPrevLineNumber(),
            	msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(),
            	dc.getThis(),
        		fsc.getFieldOwner(),
                fsc.getFieldDesc(),
                fsc.getFieldName(),
                owner,
                oldVal,
                newVal,
                fsc.isArray(),
                fsc.isPrimitive()
            );
    }

    /** PUTSTATIC **/
    @Before(marker = BytecodeMarker.class, args = "putstatic", guard = PutGuard.class, scope = Properties.SCOPE, order = 100)
    public static void beforePutStatic(LineNumberStaticContext lnsc, FieldStaticContext fsc, MethodStaticContext msc, ClassContext cc, DynamicContext dc) {
        oldVal = dc.getStaticFieldValueXXX(fsc.getFieldOwner(), fsc.getFieldName(), fsc.getFieldDesc(), Object.class);
        newVal = dc.getStackValueXXX(0, Object.class);
    }

    @AfterReturning(marker = BytecodeMarker.class, args = "putstatic", guard = PutGuard.class, scope = Properties.SCOPE, order = 100)
    public static void afterRetPutStatic(LineNumberStaticContext lnsc, FieldStaticContext fsc, MethodStaticContext msc, ClassContext cc, DynamicContext dc) {
        EventHandler.instanceOf().onStaticFieldPut(
                lnsc.getPrevLineNumber(),
//            	msc.thisClassCanonicalName()+"."+ msc.thisMethodName() + msc.thisMethodDescriptor(),
                msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(),
                dc.getThis(),
                fsc.getFieldOwner(),
                fsc.getFieldDesc(),
                fsc.getFieldName(),
                cc.asClass(fsc.getFieldOwner()),
                oldVal,
                newVal,
                fsc.isArray(),
                fsc.isPrimitive()
            );
    }

    /** ARRAY LOAD **/
    @Before(marker = BytecodeMarker.class, args = "iaload,aaload,baload,caload,daload,faload,laload,saload", guard = LoadGuard.class, scope = Properties.SCOPE, order = 100)
    public static void beforeArrayLoad(DynamicContext dc) {
        index = dc.getStackValue(0, int.class);
        arrayRef = dc.getStackValueXXX(1, Object.class);        
    }

    @AfterReturning(marker = BytecodeMarker.class, args = "iaload,aaload,baload,caload,daload,faload,laload,saload", guard = LoadGuard.class, scope = Properties.SCOPE, order = 100)
    public static void afterRetArrayLoad(LineNumberStaticContext lnsc, MethodStaticContext msc, DynamicContext dc) {
        EventHandler.instanceOf().onArrayLoad(
                lnsc.getPrevLineNumber(),
                msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(),
                dc.getThis(),
                index,
                arrayRef,
                dc.getStackValueXXX(0, Object.class)
            );        
    }

    /** ARRAY STORE **/
    //XXX OLDVAL SEMPRE A NULL
   @Before(marker = BytecodeMarker.class, args = "iastore,aastore,bastore,castore,dastore,fastore,lastore,sastore", guard = StoreGuard.class, scope = Properties.SCOPE, order = 100)
    public static void beforeArrayStore(DynamicContext dc) {
        newVal = dc.getStackValueXXX(0, Object.class);
        index = dc.getStackValue(1, int.class);
        arrayRef = dc.getStackValueXXX(2, Object.class);
        oldVal = null;
    }


//    @AfterReturning(marker = BytecodeMarker.class, args = "aastore", guard = StoreGuard.class, order = 100)
   @AfterReturning(marker = BytecodeMarker.class, args = "iastore,aastore,bastore,castore,dastore,fastore,lastore,sastore", guard = StoreGuard.class, scope = Properties.SCOPE, order = 100)
   public static void afterRetArrayStore(LineNumberStaticContext lnsc, MethodStaticContext msc, DynamicContext dc) {
        EventHandler.instanceOf().onArrayStore(
                lnsc.getPrevLineNumber(),
                msc.thisClassCanonicalName()+": "+ msc.thisMethodName() + msc.thisMethodDescriptor(),
                dc.getThis(),
                index,
                arrayRef,
                oldVal,
                newVal
            );
    }
}
