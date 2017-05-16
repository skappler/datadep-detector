package de.unisaarland.instrumentation.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BytecodeMarker;
import de.unisaarland.instrumentation.disl.guard.NoGuard;

/**
 * Instrumentation class for observing data flow across test execution through
 * static references using the DiSL/ShadowVM library.
 * 
 * 
 * - We intercept accesses and writes to ANY field by each test. This includes
 * primitives, objects, arrays and arrays of primitives.
 * 
 * - We mark each field/object as written by last test who writes it. String and
 * primitives do not have this concept
 * 
 * - If a test accesses something wrote by some other test we mark a conflict
 * and notify the event handler
 * 
 * 
 * @author gambi
 *
 */
public class Debug {

	/** GETFIELD **/
	@Before(marker = BytecodeMarker.class, args = "getfield", guard = NoGuard.class)
	public static void beforeGetField(DynamicContext dc) {
		System.out.println("Debug.beforeGetField()");
	}

}
