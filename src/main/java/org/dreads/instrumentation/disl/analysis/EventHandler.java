package org.dreads.instrumentation.disl.analysis;




public abstract class EventHandler {
    private static final String EVENT_HANDLER_PROPERTY = "disl.eventHandler";
    private static final EventHandler eh;

    static {
        EventHandler tmpEH = null;
        try {
            tmpEH = (EventHandler)
                Class.forName(
                    System.getProperty(EVENT_HANDLER_PROPERTY, "org.dreads.instrumentation.disl.analysis.MyEventHandler")
                ).newInstance();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        }
        eh = tmpEH;
    }

    public static EventHandler instanceOf() {
        return eh;
    }
    
    public abstract void onVariablePut(int tcidentity, String varname, int identity);

    public abstract void onMethodEntry(
        String fullMethodName,
        Object methodOwner
    );

    public abstract void onMethodCompletion(
        String fullMethodName,
        Object methodOwner
    );

    public abstract void onBasicBlockIn(
    	    int bbId,
    	    int entry,
    	    int exit,
    	    String classname, String methodName, String programpoint,
    	    Object methodOwner);
    
    public abstract void onBasicBlockOut(
	    int bbId,
	    int entry,
	    int exit,
	    String classname, String methodName, String programpoint,
	    Object methodOwner);

    /**************************************************
     * EXAMPLE
     * class ClassA {
     *  void foo() {
     *   ClassC.b.bar(); // the following examples refer to this line of code
     *  }
     * }
     *
     * class ClassB {
     *  void bar(int i) {
     *   ...
     *  }
     * }
     *
     * class ClassC {
     *  static ClassB b;
     * }
     **************************************************/

    /**
     * @param lineNum line number
     * @param currentMethodName full caller method name ("ClassA.foo()")
     * @param calledMethodName full callee method name ("ClassB.bar()")
     * @param caller reference to the caller object (instance of ClassA)
     * @param callee reference to the callee object (instance of ClassB)
     * @param args Object[] arguments
     * @param retVal reference to the return value
     */
    public abstract void afterMethodCall(
        int lineNum,
        String currentMethodName,
        String calledMethodName,
        Object caller,
        Object callee,
        Object[] args,
        Object retVal
    );

    /**
     * @param lineNum line number
     * @param methodName full name of the method from which the field access is performed
     * @param methodOwner reference to the object from which the field access is performed
     * @param ownerType type of the object declaring the accessed field
     * @param fieldType type of the accessed field
     * @param fieldName name of the accessed field
     * @param owner reference to the object declaring the accessed field
     * @param val reference to the current value of the field
     * @param isArray true if the accessed field is an array
     * @param isPrimitive true if the accessed field is of primitive type
     */
    public abstract void onInstanceFieldGet(
        int lineNum,
        String methodName,
        int bbIndex,
        Object methodOwner,
        String ownerType,
        String fieldType,
        String fieldName,
        Object owner,
        Object val,
        boolean isArray,
        boolean isPrimitive
    );

    /**
     * @param lineNum line number
     * @param methodName full name of the method from which the field access is performed
     * @param methodOwner reference to the object from which the field access is performed
     * @param ownerType type of the object declaring the accessed field
     * @param fieldType type of the accessed field
     * @param fieldName name of the accessed field
     * @param owner reference to the object declaring the accessed field
     * @param oldVal reference to the previous value of the field
     * @param newVal reference to the new value of the field
     * @param isArray true if the accessed field is an array
     * @param isPrimitive true if the accessed field is of primitive type
     */
    public abstract void onInstanceFieldPut(
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
        boolean isPrimitive
    );

    /**
     * @param lineNum line number
     * @param methodName full name of the method from which the field access is performed ("ClassA.foo()")
     * @param methodOwner reference to the object from which the field access is performed (instance of ClassA)
     * @param ownerType type of the object declaring the accessed field ("ClassC")
     * @param fieldType type of the accessed field ("ClassB")
     * @param fieldName name of the accessed field ("b")
     * @param owner reference to the class declaring the accessed field (ClassC)
     * @param val reference to the value of the field
     * @param isArray true if the accessed field is an array
     * @param isPrimitive true if the accessed field is of primitive type
     */
    public abstract void onStaticFieldGet(
        int lineNum,
        String methodName,
        Object methodOwner,
        String ownerType,
        String fieldType,
        String fieldName,
        Class<?> owner,
        Object val,
        boolean isArray,
        boolean isPrimitive
    );
    
    public abstract void beforeEachLine(String methodName, int linenumber); 

    /**
     * @param lineNum line number
     * @param methodName full name of the method from which the field access is performed ("ClassA.foo()")
     * @param methodOwner reference to the object from which the field access is performed (instance of ClassA)
     * @param ownerType type of the object declaring the accessed field ("ClassC")
     * @param fieldType type of the accessed field ("ClassB")
     * @param fieldName name of the accessed field ("b")
     * @param owner reference to the class declaring the accessed field (ClassC)
     * @param oldVal reference to the previous value of the field
     * @param newVal reference to the new value of the field
     * @param isArray true if the accessed field is an array
     * @param isPrimitive true if the accessed field is of primitive type
     */
    public abstract void onStaticFieldPut(
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
        boolean isPrimitive
    );

    /**
     * @param lineNum line number
     * @param methodName full name of the method from which the field access is performed ("ClassA.foo()")
     * @param methodOwner reference to the object from which the field access is performed (instance of ClassA)
     * @param index index of the accessed location in the array
     * @param arrayRef reference to the array
     * @param val reference to the value accessed in the array
     */
    public abstract void onArrayLoad(
        int lineNum,
        String methodName,
        Object methodOwner,
        int index,
        Object arrayRef,
        Object val
    );

    /**
     * @param lineNum line number
     * @param methodName full name of the method from which the field access is performed ("ClassA.foo()")
     * @param methodOwner reference to the object from which the field access is performed (instance of ClassA)
     * @param index index of the accessed location in the array
     * @param arrayRef reference to the array
     * @param oldVal reference to the previous value stored in the array
     * @param newVal reference to the new value stored in the array
     */
    public abstract void onArrayStore(
        int lineNum,
        String methodName,
        Object methodOwner,
        int index,
        Object arrayRef,
        Object oldVal,
        Object newVal
    );

	public abstract void onTestMethodCompletion(String fullMethodName);
	public abstract void onTestMethodEntry(String fullMethodName);

	public void onVariableStore(
			int lineNum,
			String methodName,
			Object methodOwner,
			String ownerType,
			String fieldType,
			String fieldName,
			Object owner,
			Object newVal,
			boolean isArray,
			boolean isPrimitive) {
		// TODO Auto-generated method stub
		
	}
	
}
