package de.unisaarland.analysis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.unisaarland.instrumentation.DependencyInfo;
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
		// TODO Move this into a guard ?!
		// /*
		// * TODO For some reason the "<Primitive/String>__DEPENDENCY_INFO"
		// fields
		// * dynamically added to classes are not intercepted by the guards...
		// */
		// if (fieldName.endsWith("__DEPENDENCY_INFO")) {
		// // System.out.println(
		// // "DataDepEventHandler.onInstanceFieldPut() Ignoring artificial
		// // tracking field " + fieldName);
		// return;
		// }
		// if (isPrimitive || fieldDesc.equals("Ljava/lang/String;")) {
		// if (ownerObject instanceof DependencyInstrumented) {
		// DependencyInfo d = extractDependencyInfo(ownerObject, fieldName);
		// System.out.println("Read primitive/String : " + fieldOwner + "." +
		// fieldName);
		// d.read();
		// System.out.println(d);
		// }
		// } else if (isArray) {
		// if (fieldObject instanceof DependencyInstrumented) {
		// DependencyInfo d = ((DependencyInstrumented)
		// fieldObject).getDEPENDENCY_INFO();
		// System.out.println("Accessed array: " + fieldOwner + "." +
		// fieldName);
		// d.read();
		// System.out.println(d);
		// }
		// } else {
		// // Regular object - What this is null ?
		// if (fieldObject instanceof DependencyInstrumented) {
		// DependencyInfo d = ((DependencyInstrumented)
		// fieldObject).getDEPENDENCY_INFO();
		// System.out.println("Accessed field: " + fieldOwner + "." +
		// fieldName);
		// d.read();
		// System.out.println(d);
		// }
		// }

	}

	public void onInstanceFieldPut(Object ownerObject, //
			String fieldOwner, String fieldDesc, String fieldName, //
			Object fieldObject, // FIXME Note that this is wrong
			boolean isArray, boolean isPrimitive) {

		// // System.out
		// // .println("DataDepEventHandler.onInstanceFieldPut() " + fieldOwner
		// + "
		// // " + fieldName + " " + fieldDesc);
		//
		// // TODO Move this into a guard ?!
		// /*
		// * TODO For some reason the "<Primitive/String>__DEPENDENCY_INFO"
		// fields
		// * dynamically added to classes are not intercepted by the guards...
		// */
		// if (fieldName.endsWith("__DEPENDENCY_INFO")) {
		// // System.out.println(
		// // "DataDepEventHandler.onInstanceFieldPut() Ignoring artificial
		// // tracking field " + fieldName);
		// return;
		// }
		//
		// if (isPrimitive || fieldDesc.equals("Ljava/lang/String;")) {
		// if (ownerObject instanceof DependencyInstrumented) {
		// System.out.println("Wrote primitive/String : " + fieldOwner + "." +
		// fieldName);
		// DependencyInfo d = extractDependencyInfo(ownerObject, fieldName);
		// d.write();
		// System.out.println(d);
		// }
		// } else if (isArray) {
		// if (fieldObject instanceof DependencyInstrumented) {
		// System.out.println("Wrote array ref: " + fieldOwner + "." +
		// fieldName);
		// DependencyInfo d = ((DependencyInstrumented)
		// fieldObject).getDEPENDENCY_INFO();
		// d.write();
		// System.out.println(d);
		// }
		// } else {
		// // Regular object - What this is null ?
		// if (fieldObject instanceof DependencyInstrumented) {
		// System.out.println("Wrote field: " + fieldOwner + "." + fieldName + "
		// of desc " + fieldDesc);
		// DependencyInfo d = ((DependencyInstrumented)
		// fieldObject).getDEPENDENCY_INFO();
		// d.write();
		// System.out.println(d);
		// }
		// }
	}

	// Static fields have no owner. Owners are instances, static fields belong
	// to classes

	public void onStaticFieldGet(String fieldOwner, String fieldDesc, String fieldName,
			//
			Object owner, //
			//
			boolean isArray, boolean isPrimitive) {

		// For some reason the PUT guard cannot stop DependencyInfo objects ?!
		if (Instrumenter.isIgnoredClass(fieldDesc)) {
			return;
		}

		// TODO null check !!! ?!
		System.out.println("DataDepEventHandler.onStaticFieldGet() " + fieldOwner + "." + fieldName + " " + owner);

		DependencyInfo d = extractStaticDependencyInfo(owner, fieldOwner, fieldName);
		if (d != null) {
			System.out.println("DataDepEventHandler.onStaticFieldPut() d " + d);
			d.read();
			System.out.println(d);
		} else {
			System.out.println("DataDepEventHandler.onStaticFieldGet() WARNING null dep info");

		}

	}

	// Static fields have no owner. Owners are instances, static fields belong
	// to classes
	public void onStaticFieldPut(String fieldOwner, String fieldDesc, String fieldName,
			//
			Object owner, //
			Object oldValue, Object newValue, // Probably they are not required
			//
			boolean isArray, boolean isPrimitive) {

		// For some reason the PUT guard cannot stop DependencyInfo objects ?!
		if (Instrumenter.isIgnoredClass(fieldDesc)) {
			return;
		}

		System.out.println("DataDepEventHandler.onStaticFieldPut() " + fieldOwner + "." + fieldName + " from "
				+ oldValue + " to " + newValue);
		DependencyInfo d = extractStaticDependencyInfo(owner, fieldOwner, fieldName);
		if (d != null) {
			System.out.println("DataDepEventHandler.onStaticFieldPut() d " + d);
			d.write();
			System.out.println(d);
		} else {
			System.out.println("DataDepEventHandler.onStaticFieldPut() WARNING null dep info");

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

	private static DependencyInfo extractDependencyInfo(Object ownerObject, String fieldName) {

		for (Method m : ownerObject.getClass().getMethods()) {
			System.out.println("DataDepEventHandler.extractDependencyInfo() Method " + m);
			if (m.getName().startsWith("get" + fieldName + "__DEPENDENCY_INFO")) {
				try {
					System.out.println("DataDepEventHandler.extractDependencyInfo() invoking " + m);
					//
					return (DependencyInfo) m.invoke(ownerObject);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		// This should never happen ....
		System.out.println("DataDepEventHandler.extractDependencyInfo() Cannot find accessor method for " + fieldName);
		return null;

	}

	private static DependencyInfo extractStaticDependencyInfo(Object owner, String fieldOwner, String fieldName) {

		if (owner == null) {
			System.out.println("DataDepEventHandler.extractStaticDependencyInfo() Null object");
			return null;
		}
		try {
			// TODO What about null ? Can it really happen?
			Class ownerClass = owner.getClass();
			for (Method m : ownerClass.getMethods()) {
				if (m.getName().startsWith("get" + fieldName + "__DEPENDENCY_INFO")) {
					System.out.println("DataDepEventHandler.extractStaticDependencyInfo() Invoking " + m);
					return (DependencyInfo) m.invoke(null, new Object[0]);
				}
			}
		} catch (Exception e) {
			System.out.println("DataDepEventHandler.extractStaticDependencyInfo() FAILED with " + e);
			e.printStackTrace();
		}
		// This should never happen ....
		return null;

	}
}
