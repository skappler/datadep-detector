package org.dreads.instrumentation.disl.analysis;

public class CollectionScope {
	// TODO TEMP, check and maybe change this. 
	// TODO Deque and Queue?
    public static boolean isValid(String className) {
        return (
            className.startsWith("java/util/")
            && (
                className.endsWith("Set")
                || className.equals("Set")
                || className.endsWith("List")
                || className.contains("Stack") 
                || className.endsWith("Vector")
                || className.endsWith("Queue") 
                || className.endsWith("Deque")
                || className.endsWith("Map")
            )
        );
    }
}
