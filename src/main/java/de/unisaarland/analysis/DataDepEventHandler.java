package de.unisaarland.analysis;

import de.unisaarland.instrumentation.DependencyInfo;
import de.unisaarland.instrumentation.DependencyInstrumented;
import de.unisaarland.instrumentation.Instrumenter;

public class DataDepEventHandler {

	// TODO how to handle null values assignments and reads ? Probably looking
	// at the values before/after and calling the read/write methods and
	// conflicts on the non null value read or to be read
	// TODO How to handle modifications to objects inside arrays... are arrays
	// considered modified if any of their elements is ?

	// FIXME Handle Test LifeCycle Events, store map tests to testID and such,
	// update
	// test counters

	public static final int CURRENT_TEST = 0;

	// Handle conflict events if any
	public void onConflict(/* Some data here .... */) {

	}

	private static DataDepEventHandler INSTANCE;

	private DataDepEventHandler() {
		// TODO Auto-generated constructor stub
	}

	public static synchronized DataDepEventHandler instanceOf() {
		if (INSTANCE == null)
			INSTANCE = new DataDepEventHandler();
		return INSTANCE;
	}

	// TODO does it make any difference if the field is static ?
	public void onInstanceFieldGet(Object ownerObject, //
			String fieldOwner, String fieldDesc, String fieldName, //
			Object fieldObject, //
			boolean isArray, boolean isPrimitive) {

		if (isPrimitive || fieldObject instanceof String) {
			if (ownerObject instanceof DependencyInstrumented) {
				System.out.println("Access of primitive/String fields not implemented");
				// TODO How to access those additional fields ?
				// DependencyInfo d = ((DependencyInstrumented)
				// fieldObject).getDEPENDENCY_INFO();
				// System.out.println("Accessed array: " + fieldOwner + "." +
				// fieldName);
				// d.read();
			}
		} else if (isArray) {
			if (fieldObject instanceof DependencyInstrumented) {
				DependencyInfo d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
				System.out.println("Accessed array: " + fieldOwner + "." + fieldName);
				d.read();
			}
		} else {
			// Regular object - What this is null ?
			if (fieldObject instanceof DependencyInstrumented) {
				DependencyInfo d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
				System.out.println("Accessed field: " + fieldOwner + "." + fieldName);
				d.read();
			}
		}

	}

	public void onInstanceFieldPut(Object ownerObject, //
			String fieldOwner, String fieldDesc, String fieldName, //
			Object fieldObject, //
			boolean isArray, boolean isPrimitive) {

		/*
		 * TODO For some reason the "<Primitive/String>__DEPENDENCY_INFO" fields
		 * dynamically added to classes are not intercepted by the guards...
		 */
		if (Instrumenter.isIgnoredClass(fieldDesc)) {
			// System.out.println("DataDepEventHandler.onInstanceFieldPut()
			// Ignoring " + fieldOwner + "." + fieldName
			// + " of type " + fieldDesc);
			return;
		}

		if (isPrimitive || fieldObject instanceof String) {
			if (ownerObject instanceof DependencyInstrumented) {
				System.out.println("Access of primitive/String fields not implemented for " + fieldOwner + "."
						+ fieldName + " -- " + fieldDesc);
				// TODO How to access those additional fields ?
				// DependencyInfo d = ((DependencyInstrumented)
				// fieldObject).getDEPENDENCY_INFO();
				// System.out.println("Accessed array: " + fieldOwner + "." +
				// fieldName);
				// d.write();
			}
		} else if (isArray) {
			if (fieldObject instanceof DependencyInstrumented) {
				DependencyInfo d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
				System.out.println("Wrote array ref: " + fieldOwner + "." + fieldName);
				d.write();
			}
		} else {
			// Regular object - What this is null ?
			if (fieldObject instanceof DependencyInstrumented) {
				DependencyInfo d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
				System.out.println("Wrote field: " + fieldOwner + "." + fieldName + " of desc " + fieldDesc);
				d.write();
			}
		}
	}

	// Static fields have no owner. Owners are instances, static fields belong
	// to classes

	public void onStaticFieldGet(String fieldOwner, String fieldDesc, String fieldName, Object fieldObject,
			boolean isArray, boolean isPrimitive) {

		/*
		 * TODO For some reason System.out is not filtered out by the
		 * guard...actually, it is not even checked for inclusion... Can be a
		 * bug ? Anyway, it seems that System.out is actually instrumented, that
		 * is, deps info is there... why so ?!
		 */
		if (Instrumenter.isIgnoredClass(fieldOwner)) {
			// System.out.println("DataDepEventHandler.onStaticFieldGet()
			// Ignoring " + fieldOwner + "." + fieldName);
			return;
		}

		// TODO Auto-generated method stub
		if (isPrimitive || fieldObject instanceof String) {
			// TODO How to access those static primitive fields then ?!
			// DependencyInfo d = ((DependencyInstrumented)
			// fieldObject).getDEPENDENCY_INFO();
			// System.out.println("Accessed array: " + fieldOwner + "." +
			// fieldName);
			// d.read();
		} else if (isArray) {
			if (fieldObject instanceof DependencyInstrumented) {
				DependencyInfo d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
				System.out.println("Accessed static array: " + fieldOwner + "." + fieldName);
				d.read();
			}
		} else {
			// Regular object - What this is null ?
			if (fieldObject instanceof DependencyInstrumented) {
				DependencyInfo d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
				System.out.println("Accessed static field: " + fieldOwner + "." + fieldName);
				d.read();
			}
		}

	}

	// Static fields have no owner. Owners are instances, static fields belong
	// to classes
	public void onStaticFieldPut(String fieldOwner, String fieldDesc, String fieldName, Object fieldObject,
			boolean isArray, boolean isPrimitive) {
		/*
		 * TODO For some reason System.out is not filtered out by the
		 * guard...actually, it is not even checked for inclusion... Can be a
		 * bug ? Anyway, it seems that System.out is actually instrumented, that
		 * is, deps info is there... why so ?!
		 */
		if (Instrumenter.isIgnoredClass(fieldOwner)) {
			return;
		}

		if (isPrimitive || fieldObject instanceof String) {
			// TODO How to access those static primitive fields then ?!
			// DependencyInfo d = ((DependencyInstrumented)
			// fieldObject).getDEPENDENCY_INFO();
			// System.out.println("Accessed array: " + fieldOwner + "." +
			// fieldName);
			// d.read();
		} else if (isArray) {
			if (fieldObject instanceof DependencyInstrumented) {
				DependencyInfo d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
				System.out.println("Wrote static array: " + fieldOwner + "." + fieldName);
				d.write();
			}
		} else {
			// Regular object - What this is null ?
			if (fieldObject instanceof DependencyInstrumented) {
				DependencyInfo d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
				System.out.println("Wrote static field: " + fieldOwner + "." + fieldName);
				d.write();
			}
		}

	}

	// TODO No idea what arrayRef is compared to fieldValue...
	// FIXME Handle arrays of primitives !
	public void onArrayStore(int index, Object arrayRef, String fieldOwner, String fieldDesc, String fieldName,
			Object fieldObject, boolean isArray, boolean isPrimitive) {
		// TODO Auto-generated method stub
		System.out.println("On Array Store : " + index + " " + arrayRef + " " + fieldOwner + "." + fieldName);

	}

	public void onArrayLoad(int index, Object arrayRef, String fieldOwner, String fieldDesc, String fieldName,
			Object fieldObject, boolean isArray, boolean isPrimitive) {
		// TODO Auto-generated method stub
		System.out.println("On Array Load : " + index + " " + arrayRef + " " + fieldOwner + "." + fieldName);

	}
}
