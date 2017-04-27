package org.dreads.instrumentation.disl.analysis;

import org.dreads.analysis.config.Properties;
import org.dreads.analysis.runtime.Recorder;

public class MyEventHandler extends EventHandler {
    
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				ch.usi.dag.disl.dynamicbypass.DynamicBypass.activate(); // when running with full-coverage
				System.out.println("On shutdown...");
				Recorder.instance.computeAndExportDataFlowInfo();
				}
		});
	}
/////////////////Threadlocal? 
	private enum Event {
        METHOD_ENTRY,
        METHOD_COMPLETION,
        METHOD_CALL,
        BASICBLOCK_ENTRY,
        BASICBLOCK_EXIT,
        INSTANCE_FIELD_GET,
        INSTANCE_FIELD_PUT,
        STATIC_FIELD_GET,
        STATIC_FIELD_PUT,
        ARRAY_LOAD,
        TEST_LINE,
        ARRAY_STORE,
        VARIABLE_ASSIGN,
        TEST_EXIT,
        TEST_ENTRY
    };

    private static final String INDENTATION = "\t";

    @Override
    public void onMethodEntry(String fullMethodName, Object methodOwner) {
        println(Event.METHOD_ENTRY, fullMethodName);
        Recorder.instance.onMethodEntry(fullMethodName, methodOwner);
    }
    
	@Override
	public void beforeEachLine(String methodName, int linenumber) {
		printalways(Event.TEST_LINE, methodName + " " + linenumber);
		Recorder.instance.beforeEachLine(methodName, linenumber);
	}

    @Override
    public void onMethodCompletion(String fullMethodName, Object methodOwner) {
        println(Event.METHOD_COMPLETION, fullMethodName);
        Recorder.instance.onMethodCompletion(fullMethodName, methodOwner);
    }

	@Override
	public void onBasicBlockIn(int bbId, int entry, int exit, String classname, String methodName, String programpoint, Object methodOwner) {
		println(Event.BASICBLOCK_ENTRY, bbId);
		Recorder.instance.onBasicBlockIn(bbId, entry, exit, classname, methodName, programpoint, methodOwner);
	}

	@Override
	public void onBasicBlockOut(int bbId, int entry, int exit, String classname, String methodName, String programpoint, Object methodOwner) {
		println(Event.BASICBLOCK_EXIT, bbId);
		Recorder.instance.onBasicBlockOut(bbId, entry, exit, classname, methodName, programpoint, methodOwner);
	}
    @Override
    public void onVariablePut(int tcidentity, String varname, int identity) {
    	println(Event.VARIABLE_ASSIGN, varname);
    	Recorder.instance.onVariablePut(tcidentity, varname, identity);
    	
    }
    @Override
    public void afterMethodCall(
            int lineNum,
            String currentMethodName,
            String calledMethodName,
            Object caller,
            Object callee,
            Object[] args,
            Object retVal) {
        println(Event.METHOD_CALL, lineNum, currentMethodName, calledMethodName, caller, callee, args, retVal);
        Recorder.instance.afterMethodCall(lineNum, currentMethodName, calledMethodName, caller, callee, args, retVal);
    }

    @Override
    public void onInstanceFieldGet(
            int lineNum,
            String methodName,
            int bbsize,
            Object methodOwner,
            String ownerType,
            String fieldType,
            String fieldName,
            Object owner,
            Object val,
            boolean isArray,
            boolean isPrimitive) {
        println(Event.INSTANCE_FIELD_GET, lineNum, methodName, methodOwner, ownerType, fieldType, fieldName, owner, val, isArray, isPrimitive);
        Recorder.instance.onInstanceFieldGet(lineNum, methodName, bbsize, methodOwner, ownerType, fieldType, fieldName, owner, val, isArray, isPrimitive);
    }

    @Override
    public void onInstanceFieldPut(
            int lineNum,
            String methodName,
            Object methodOwner,
            String ownerType,
            String fieldType,
            String fieldName,
            Object owner,
            Object oldVal,
            Object newVal,
            boolean isArray,
            boolean isPrimitive) {
        println(Event.INSTANCE_FIELD_PUT, lineNum, methodName, methodOwner, ownerType, fieldType, fieldName, owner, oldVal, newVal, isArray, isPrimitive);
        Recorder.instance.onInstanceFieldPut(lineNum, methodName, methodOwner, ownerType, fieldType, fieldName, owner, oldVal, newVal, isArray, isPrimitive);
    }
    
   
    @Override
    public void onStaticFieldGet(
            int lineNum,
            String methodName,
            Object methodOwner,
            String ownerType,
            String fieldType,
            String fieldName,
            Class<?> owner,
            Object val,
            boolean isArray,
            boolean isPrimitive) {
        println(Event.STATIC_FIELD_GET, lineNum, methodName, methodOwner, ownerType, fieldType, fieldName, owner, val, isArray, isPrimitive);
    }

    @Override
    public void onStaticFieldPut(
            int lineNum,
            String methodName,
            Object methodOwner,
            String ownerType,
            String fieldType,
            String fieldName,
            Class<?> owner,
            Object oldVal,
            Object newVal,
            boolean isArray,
            boolean isPrimitive) {
        println(Event.STATIC_FIELD_PUT, lineNum, methodName, methodOwner, ownerType, fieldType, fieldName, owner, oldVal, newVal, isArray, isPrimitive);
    }

    @Override
    public void onArrayLoad(
            int lineNum,
            String methodName,
            Object methodOwner,
            int index,
            Object arrayRef,
            Object val) {
        println(Event.ARRAY_LOAD, lineNum, methodName, methodOwner, index, arrayRef.getClass().getName(), val);
        Recorder.instance.onArrayLoad(lineNum, methodName, methodOwner, index, arrayRef, val);
    }

    //XXX OLDVAL SEMPRE A NULL
    @Override
    public void onArrayStore(
            int lineNum,
            String methodName,
            Object methodOwner,
            int index,
            Object arrayRef,
            Object oldVal,
            Object newVal) {
        println(Event.ARRAY_STORE, lineNum, methodName, methodOwner, index,  arrayRef.getClass().getName(), oldVal, newVal);
        Recorder.instance.onArrayStore(lineNum, methodName, methodOwner, index, arrayRef, oldVal, newVal);
    }

    private static void println(Event evt, Object... objs) {
    	if(Properties.SYSOPRINT){
    	System.out.println("\n" + evt.name());
        for(Object obj : objs) {
            try {
				System.out.println(INDENTATION + obj);
			} catch (Exception e) {
				continue;
			}
        }
    	}
    }
    
    private static void printalways(Event evt, Object... objs) {
     
    	System.out.println("\n" + evt.name());
        for(Object obj : objs) {
            try {
				System.out.println(INDENTATION + obj);
			} catch (Exception e) {
				continue;
			}
        }
    	
    }

    @Override
	public void onTestMethodEntry(String fullMethodName) {
		printalways(Event.TEST_ENTRY);
		Recorder.instance.onTestEntry(fullMethodName);
		
	}
    
	@Override
	public void onTestMethodCompletion(String fullMethodName) {
		printalways(Event.TEST_EXIT);
		Recorder.instance.onTestExit(fullMethodName);
	}
}
