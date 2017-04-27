package org.dreads.instrumentation.disl.analysis;

import org.dreads.analysis.config.Properties;

public class BaseProgramScope {
    public static boolean isValid(String className) {
        return !((className.startsWith("java/")
            || className.startsWith("javax/")
            || className.startsWith("com/google/")
            || className.startsWith("com/sun/")
            || className.startsWith("org/eclipse")
            || className.startsWith("com/apple/")
            || className.startsWith("sun/")
            || className.startsWith("sunx/")
            || className.startsWith("org/jgrapht/")
            || className.startsWith("org/jcp/")
            || className.startsWith("org/omg/")
            || className.startsWith("org/xml/")
            || className.startsWith("org/ietf/")
            || className.startsWith("org/junit/")
            || className.startsWith("$Proxy")
            || checkFilter(className)
            || className.toLowerCase().contains(Properties.TESTCLASS))
        );
    }
    
    private static boolean checkFilter(String classname){
    	for (String s : Properties.filteredClasses) {
			if(classname.contains(s)) return true;
		}
    	return false;
    }
    
    public static boolean isValidTest(String className) {
        return (!(className.startsWith("java/")
            || className.startsWith("javax/")
            || className.startsWith("com/google/")
            || className.startsWith("com/sun/")
            || className.startsWith("org/eclipse")
            || className.startsWith("com/apple/")
            || className.startsWith("sun/")
            || className.startsWith("sunx/")
            || className.startsWith("org/jgrapht/")
            || className.startsWith("org/jcp/")
            || className.startsWith("org/omg/")
            || className.startsWith("org/xml/")
            || className.startsWith("org/ietf/")
            || className.startsWith("org/junit/")
            || className.startsWith("$Proxy")
        )&&(className.toLowerCase().endsWith("test")||className.toLowerCase().contains("evosuite")||className.toLowerCase().contains("dreadstest")||className.toLowerCase().contains("btest")||className.toLowerCase().endsWith("tests")||className.toLowerCase().contains("testsuite")||className.toLowerCase().contains("/test")||className.toLowerCase().contains("randoop")) );
    }
}
