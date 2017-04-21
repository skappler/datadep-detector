package de.unisaarland.instrumentation.disl;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import de.unisaarland.instrumentation.DependencyInfo;
import de.unisaarland.instrumentation.DependencyInstrumented;
import de.unisaarland.instrumentation.Instrumenter;
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

	/** GETFIELD **/
	@AfterReturning(marker = BytecodeMarker.class, args = "getfield", //
			guard = GetGuard.class)
	public static void afterRetGetField(FieldStaticContext fsc, //
			MethodStaticContext msc, //
			DynamicContext dc) {

		if (dc.getThis() instanceof DependencyInstrumented) {
			DependencyInfo d = ((DependencyInstrumented) dc.getThis()).getDEPENDENCY_INFO();
			System.out.println("Read (getfield) " + fsc.getFieldOwner() + " " + fsc.getFieldDesc() + fsc.getFieldName()
					+ " --- " + "Dep info : " + d);
			d.read();
		} else {
			System.out.println("ConflictDetection.afterRetGetField() " + fsc.getFieldOwner() + " " + fsc.getFieldDesc()
					+ fsc.getFieldName());
		}

	}

	/** GETSTATIC **/
	@AfterReturning(marker = BytecodeMarker.class, args = "getstatic", //
			guard = GetGuard.class)
	public static void afterRetGetStatic(FieldStaticContext fsc, MethodStaticContext msc, ClassContext cc,
			DynamicContext dc) {
		if (dc.getThis() instanceof DependencyInstrumented) {
			DependencyInfo d = ((DependencyInstrumented) dc.getThis()).getDEPENDENCY_INFO();
			System.out.println("Read (getstatic) " + fsc.getFieldOwner() + " " + fsc.getFieldDesc() + fsc.getFieldName()
					+ " --- " + "Dep info : " + d);
			d.read();
		} else {
			System.out.println("ConflictDetection.afterRetGetStatic() " + fsc.getFieldOwner() + " " + fsc.getFieldDesc()
					+ fsc.getFieldName());
		}
	}

	/** PUTFIELD **/
	@AfterReturning(marker = BytecodeMarker.class, args = "putfield", //
			guard = PutGuard.class)
	public static void afterRetPutField(FieldStaticContext fsc, MethodStaticContext msc, DynamicContext dc) {
		if (dc.getThis() instanceof DependencyInstrumented) {
			DependencyInfo d = ((DependencyInstrumented) dc.getThis()).getDEPENDENCY_INFO();
			System.out.println("Write (putfield) " + fsc.getFieldOwner() + " " + fsc.getFieldDesc() + fsc.getFieldName()
					+ " --- " + "Dep info : " + d);
			d.write();
		} else {
			System.out.println("ConflictDetection.afterRetPutField() " + fsc.getFieldOwner() + " " + fsc.getFieldDesc()
					+ fsc.getFieldName());
		}
	}

	/** PUTSTATIC **/
	@AfterReturning(marker = BytecodeMarker.class, args = "putstatic", //
			guard = PutGuard.class)
	public static void afterRetPutStatic(FieldStaticContext fsc, MethodStaticContext msc, ClassContext cc,
			DynamicContext dc) {

		if (dc.getThis() instanceof DependencyInstrumented) {
			DependencyInfo d = ((DependencyInstrumented) dc.getThis()).getDEPENDENCY_INFO();
			System.out.println("Write (putstatic) " + fsc.getFieldOwner() + " " + fsc.getFieldDesc()
					+ fsc.getFieldName() + " --- " + "Dep info : " + d);
			d.write();
		} else {
			System.out.println("ConflictDetection.afterRetArrayLoad() putstatic of " + fsc.getFieldOwner() + " "
					+ fsc.getFieldDesc() + fsc.getFieldName());
		}
	}

	// TODO Arrays will take care of them later...
	/** ARRAY LOAD **/
	@AfterReturning(marker = BytecodeMarker.class, args = "iaload,aaload,baload,caload,daload,faload,laload,saload", //
			guard = LoadGuard.class)
	public static void afterRetArrayLoad(FieldStaticContext fsc, MethodStaticContext msc, DynamicContext dc) {
		System.out.println("ConflictDetection.afterRetArrayLoad() " + fsc.getFieldOwner() + " " + fsc.getFieldDesc()
				+ " " + fsc.getFieldName());
	}

	/** ARRAY STORE **/
	@AfterReturning(marker = BytecodeMarker.class, args = "iastore,aastore,bastore,castore,dastore,fastore,lastore,sastore", //
			guard = StoreGuard.class)
	public static void afterRetArrayStore(FieldStaticContext fsc, MethodStaticContext msc, DynamicContext dc) {
		System.out.println("ConflictDetection.afterRetArrayStore() " + fsc.getFieldOwner() + " " + fsc.getFieldDesc()
				+ " " + fsc.getFieldName());
	}

}
