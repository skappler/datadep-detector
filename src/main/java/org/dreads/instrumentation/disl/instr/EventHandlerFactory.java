package org.dreads.instrumentation.disl.instr;

import org.dreads.instrumentation.disl.analysis.EventHandler;

public class EventHandlerFactory {
    private static final String EVENT_HANDLER_PROPERTY = "disl.eventHandler";
    private static final EventHandler eh;

    static {
        EventHandler tmpEH = null;
        try {
            tmpEH = (EventHandler)
                Class.forName(
                    System.getProperty(EVENT_HANDLER_PROPERTY)
                ).newInstance();
        } catch (Throwable t) {
        	System.err.println("Check the value of -D" + EVENT_HANDLER_PROPERTY);
            t.printStackTrace();
            System.exit(-1);
        }
        eh = tmpEH;
    }

    public static EventHandler getEventHandler() {
        return eh;
    }
}
