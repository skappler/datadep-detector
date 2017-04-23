package de.unisaarland.instrumentation.disl;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import de.unisaarland.analysis.DataDepEventHandler;
import de.unisaarland.instrumentation.DependencyInfo;
import de.unisaarland.instrumentation.disl.context.FieldStaticContext;
import de.unisaarland.instrumentation.guard.GetGuard;
import de.unisaarland.instrumentation.guard.LoadGuard;
import de.unisaarland.instrumentation.guard.PutGuard;
import de.unisaarland.instrumentation.guard.StoreGuard;

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
public class ConflictDetection {

	// FIXME Check upon which object are we calling the read/write it must be
	// the field if not primitives/String, or the right depInfo in the owner
	// otherwise
	@SyntheticLocal
	static Object owner;
	// @SyntheticLocal static Object oldVal;
	// @SyntheticLocal static Object newVal;
	@SyntheticLocal
	static Object arrayRef;
	@SyntheticLocal
	static int index;

	/**
	 * Test Lifecycle. TODO: For this implementation I assume that tests are
	 * executed as Requests to JUnitCore. One test method corresponds to one
	 * request. Otherwise, it would be enough to register an event handler like
	 * PreparingEventListener
	 **/
	@Before(marker = BodyMarker.class, scope = "org.junit.runner.JUnitCore.run(org.junit.runner.Request)")
	public static void beforeTestExecution() {
		// Notify Event Handler here
		DependencyInfo.CURRENT_TEST++;
		DependencyInfo.IN_CAPTURE = true;
		System.out.println("ConflictDetection.beforeTestExecution() Next test is: " + DependencyInfo.CURRENT_TEST);
	}

	@AfterReturning(marker = BodyMarker.class, scope = "org.junit.runner.JUnitCore.run(org.junit.runner.Request)")
	public static void afterTestExecution() {
		DependencyInfo.IN_CAPTURE = false;
		// Notify Event Handler here
		System.out.println("ConflictDetection.afterTestExecution() Finished test : " + DependencyInfo.CURRENT_TEST);
	}

	/** GETFIELD **/
	@Before(marker = BytecodeMarker.class, args = "getfield", //
			guard = GetGuard.class,
			// TODO What's SCOPE ?
			order = 100)
	public static void beforeGetField(FieldStaticContext fsc, DynamicContext dc) {
		owner = dc.getStackValue(0, Object.class);
	}

	@AfterReturning(marker = BytecodeMarker.class, args = "getfield", //
			guard = GetGuard.class)
	public static void afterRetGetField(FieldStaticContext fsc, //
			MethodStaticContext msc, //
			DynamicContext dc) {

		DataDepEventHandler.instanceOf().onInstanceFieldGet(owner, fsc.getFieldOwner(), fsc.getFieldDesc(),
				fsc.getFieldName(), dc.getThis(), fsc.isArray(), fsc.isPrimitive());

	}

	/** GETSTATIC **/
	@AfterReturning(marker = BytecodeMarker.class, args = "getstatic", //
			guard = GetGuard.class)
	public static void afterRetGetStatic(FieldStaticContext fsc, MethodStaticContext msc, ClassContext cc,
			DynamicContext dc) {
		DataDepEventHandler.instanceOf().onStaticFieldGet(fsc.getFieldOwner(), fsc.getFieldDesc(), fsc.getFieldName(),
				dc.getThis(), fsc.isArray(), fsc.isPrimitive());
	}

	/** PUTFIELD **/
	@Before(marker = BytecodeMarker.class, args = "putfield", guard = PutGuard.class, order = 100)
	public static void beforePutField(FieldStaticContext fsc, MethodStaticContext msc, DynamicContext dc) {
		owner = dc.getStackValue(1, Object.class);
	}

	@AfterReturning(marker = BytecodeMarker.class, args = "putfield", //
			guard = PutGuard.class)
	public static void afterRetPutField(FieldStaticContext fsc, MethodStaticContext msc, DynamicContext dc) {
		DataDepEventHandler.instanceOf().onInstanceFieldPut(owner, fsc.getFieldOwner(), fsc.getFieldDesc(),
				fsc.getFieldName(), dc.getThis(), fsc.isArray(), fsc.isPrimitive());
	}

	/** PUTSTATIC **/
	// Before only if we need to check values
	@AfterReturning(marker = BytecodeMarker.class, args = "putstatic", //
			guard = PutGuard.class)
	public static void afterRetPutStatic(FieldStaticContext fsc, MethodStaticContext msc, ClassContext cc,
			DynamicContext dc) {

		DataDepEventHandler.instanceOf().onStaticFieldPut(fsc.getFieldOwner(), fsc.getFieldDesc(), fsc.getFieldName(),
				dc.getThis(), fsc.isArray(), fsc.isPrimitive());
	}

	// TODO Arrays will take care of them later...
	/** ARRAY LOAD **/
	@Before(marker = BytecodeMarker.class, args = "iaload,aaload,baload,caload,daload,faload,laload,saload", guard = LoadGuard.class)
	public static void beforeArrayLoad(DynamicContext dc) {
		index = dc.getStackValue(0, int.class);
		// arrayRef = dc.getStackValueXXX(1, Object.class);
		arrayRef = dc.getStackValue(1, Object.class);
	}

	@AfterReturning(marker = BytecodeMarker.class, args = "iaload,aaload,baload,caload,daload,faload,laload,saload", //
			guard = LoadGuard.class)
	public static void afterRetArrayLoad(FieldStaticContext fsc, MethodStaticContext msc, DynamicContext dc) {
		DataDepEventHandler.instanceOf().onArrayLoad(index, arrayRef, fsc.getFieldOwner(), fsc.getFieldDesc(),
				fsc.getFieldName(), dc.getThis(), fsc.isArray(), fsc.isPrimitive());
	}

	/** ARRAY STORE **/
	// TODO When this actually triggers ?!
	@Before(marker = BytecodeMarker.class, args = "iastore,aastore,bastore,castore,dastore,fastore,lastore,sastore", guard = StoreGuard.class)
	public static void beforeArrayStore(DynamicContext dc) {
		// newVal = dc.getStackValueXXX(0, Object.class);
		// TODO What's this XXX thingy ?!
		// arrayRef = dc.getStackValueXXX(2, Object.class);
		// oldVal = null;
		// No owner here ?!
		index = dc.getStackValue(1, int.class);
		arrayRef = dc.getStackValue(2, Object.class);
	}

	@AfterReturning(marker = BytecodeMarker.class, args = "iastore,aastore,bastore,castore,dastore,fastore,lastore,sastore", //
			guard = StoreGuard.class)
	public static void afterRetArrayStore(FieldStaticContext fsc, MethodStaticContext msc, DynamicContext dc) {
		DataDepEventHandler.instanceOf().onArrayStore(index, arrayRef, fsc.getFieldOwner(), fsc.getFieldDesc(),
				fsc.getFieldName(), dc.getThis(), fsc.isArray(), fsc.isPrimitive());
	}

}
