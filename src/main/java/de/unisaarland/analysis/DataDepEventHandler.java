package de.unisaarland.analysis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public static HashMap<Integer, String> testNumToMethod = new HashMap<Integer, String>();
	public static HashMap<Integer, String> testNumToTestClass = new HashMap<>();

	private Set<DependencyInfo> conflicts;

	// TODO Data Deps
	private List<DataDependency> dataDependencies;

	private static DataDepEventHandler INSTANCE;

	private DataDepEventHandler() {
		conflicts = new HashSet<DependencyInfo>();
		dataDependencies = new ArrayList<DataDependency>();
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

		if (fieldName.endsWith("__DEPENDENCY_INFO")) {
			return;
		}
		DependencyInfo d = null;
		if (isPrimitive || fieldDesc.equals("Ljava/lang/String;")) {
			if (ownerObject instanceof DependencyInstrumented) {
				d = extractDependencyInfo(ownerObject, fieldName);
				System.out.println("Read primitive/String : " + fieldOwner + "." + fieldName);
			}
		} else if (isArray) {
			if (fieldObject instanceof DependencyInstrumented) {
				System.out.println("Read array: " + fieldOwner + "." + fieldName);
				d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
			}
		} else {
			// Regular object - What this is null ?
			if (fieldObject instanceof DependencyInstrumented) {
				System.out.println("Read field: " + fieldOwner + "." + fieldName);
				d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
			}
		}

		if (d != null) {
			if (d.read()) {
				int sourceTestID = d.getLastWrite();
				//
				conflicts.add(d);
				DataDependency dataDependency = new DataDependency(fieldOwner, fieldName, //
						sourceTestID, DependencyInfo.CURRENT_TEST, //
						testNumToTestClass.get(sourceTestID) + "." + testNumToMethod.get(sourceTestID),
						testNumToTestClass.get(DependencyInfo.CURRENT_TEST) + "."
								+ testNumToMethod.get(DependencyInfo.CURRENT_TEST));
				dataDependencies.add(dataDependency);
				// Print out the Conflict data
				// System.out.println(dataDependency);
			}
		} else {
			System.out.println("DataDepEventHandler.onStaticFieldGet() WARNING null dep info");

		}

	}

	public void onInstanceFieldPut(Object ownerObject, //
			String fieldOwner, String fieldDesc, String fieldName, //
			Object fieldObject, // FIXME Note that does not always correspond to
								// the field object ...
			boolean isArray, boolean isPrimitive) {

		/*
		 * TODO For some reason the "<Primitive/String>__DEPENDENCY_INFO" fields
		 * dynamically added to classes are not intercepted by the guards...
		 */
		if (fieldName.endsWith("__DEPENDENCY_INFO")) {
			return;
		}

		DependencyInfo d = null;

		System.out.println(
				"DataDepEventHandler.onInstanceFieldPut() " + fieldOwner + "." + fieldName + " " + ownerObject);
		if (isPrimitive || fieldDesc.equals("Ljava/lang/String;")) {
			if (ownerObject instanceof DependencyInstrumented) {
				System.out.println("Wrote primitive/String : " + fieldOwner + "." + fieldName);
				d = extractDependencyInfo(ownerObject, fieldName);
			}
		} else if (isArray) {
			if (fieldObject instanceof DependencyInstrumented) {
				System.out.println("Wrote array ref: " + fieldOwner + "." + fieldName);
				d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
			}
		} else {
			// Regular object - What this is null ?
			if (fieldObject instanceof DependencyInstrumented) {
				System.out.println("Wrote field: " + fieldOwner + "." + fieldName + " of desc " + fieldDesc);
				d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
			}
		}
		if (d != null) {
			if (d.write()) {
				int sourceTestID = d.getLastWrite();
				//
				conflicts.add(d);
				DataDependency dataDependency = new DataDependency(fieldOwner, fieldName, //
						sourceTestID, DependencyInfo.CURRENT_TEST, //
						testNumToTestClass.get(sourceTestID) + "." + testNumToMethod.get(sourceTestID),
						testNumToTestClass.get(DependencyInfo.CURRENT_TEST) + "."
								+ testNumToMethod.get(DependencyInfo.CURRENT_TEST));
				dataDependencies.add(dataDependency);
				// Print out the Conflict data
				// System.out.println(dataDependency);
			}
		} else {
			System.out.println("DataDepEventHandler.onStaticFieldGet() WARNING null dep info");

		}
	}

	// Static fields have no owner. Owners are instances, static fields belong
	// to classes

	public void onStaticFieldGet(String fieldOwner, String fieldDesc, String fieldName,
			//
			Object owner, //
			//
			boolean isArray, boolean isPrimitive) {

		// For some reason the GET guard cannot stop DependencyInfo objects ?!
		if (Instrumenter.isIgnoredClass(fieldDesc)) {
			return;
		}

		System.out.println("DataDepEventHandler.onStaticFieldGet() " + fieldOwner + "." + fieldName + " " + owner);

		DependencyInfo d = extractStaticDependencyInfo(owner, fieldOwner, fieldName);
		if (d != null) {
			if (d.read()) {
				int sourceTestID = d.getLastWrite();
				//
				conflicts.add(d);
				DataDependency dataDependency = new DataDependency(fieldOwner, fieldName, //
						sourceTestID, DependencyInfo.CURRENT_TEST, //
						testNumToTestClass.get(sourceTestID) + "." + testNumToMethod.get(sourceTestID),
						testNumToTestClass.get(DependencyInfo.CURRENT_TEST) + "."
								+ testNumToMethod.get(DependencyInfo.CURRENT_TEST));
				dataDependencies.add(dataDependency);
				// Print out the Conflict data
				// System.out.println(dataDependency);
			}
		} else {
			System.out.println(
					"DataDepEventHandler.onStaticFieldGet() WARNING null dep info for " + fieldOwner + "/" + fieldName);
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

		// System.out.println("DataDepEventHandler.onStaticFieldPut() " +
		// fieldOwner + "." + fieldName + " from "
		// + oldValue + " to " + newValue);
		DependencyInfo d = extractStaticDependencyInfo(owner, fieldOwner, fieldName);
		if (d != null) {
			// System.out.println("DataDepEventHandler.onStaticFieldPut() d " +
			// d);
			if (d.write()) {
				int sourceTestID = d.getLastWrite();
				//
				conflicts.add(d);
				DataDependency dataDependency = new DataDependency(fieldOwner, fieldName, //
						sourceTestID, DependencyInfo.CURRENT_TEST, //
						testNumToTestClass.get(sourceTestID) + "." + testNumToMethod.get(sourceTestID),
						testNumToTestClass.get(DependencyInfo.CURRENT_TEST) + "."
								+ testNumToMethod.get(DependencyInfo.CURRENT_TEST));
				dataDependencies.add(dataDependency);
				// Print out the Conflict data
				// System.out.println(dataDependency);
			}
		} else {
			System.out.println(
					"DataDepEventHandler.onStaticFieldPut() WARNING null dep info for " + fieldOwner + "/" + fieldName);
		}

	}

	// TODO will those triggers read/write inside arrays or of the array ref ?!
	// TODO No idea what arrayRef is compared to fieldValue...
	// FIXME Handle arrays of primitives !
	public void onArrayStore(int index, Object arrayRef, String fieldOwner, String fieldDesc, String fieldName,
			Object fieldObject) {
		// TODO Auto-generated method stub
		System.out.println("On Array Store : " + index + " " + arrayRef + " " + fieldOwner + "." + fieldName);

	}

	public void onArrayLoad(int index, Object arrayRef, String fieldOwner, String fieldDesc, String fieldName,
			Object fieldObject) {
		// TODO Auto-generated method stub
		System.out.println("On Array Load : " + index + " " + arrayRef + " " + fieldOwner + "." + fieldName);

	}

	private static DependencyInfo extractDependencyInfo(Object ownerObject, String fieldName) {
		try {
			for (Method m : ownerObject.getClass().getMethods()) {
				if (m.getName().startsWith("get" + fieldName + "__DEPENDENCY_INFO")) {
					// System.out.println("DataDepEventHandler.extractDependencyInfo()
					// invoking " + m);
					return (DependencyInfo) m.invoke(ownerObject);
				}
			}
		} catch (Exception e) {
			System.out.println("DataDepEventHandler.extractDependencyInfo() FAILED with " + e);
			e.printStackTrace();
		}
		// This should never happen ....
		System.out.println("DataDepEventHandler.extractDependencyInfo() Cannot find accessor method for " + fieldName);
		return null;

	}

	private static DependencyInfo extractStaticDependencyInfo(Object owner, String fieldOwner, String fieldName) {

		if (owner == null) {
			// System.out.println("DataDepEventHandler.extractStaticDependencyInfo()
			// Null object");
			return null;
		}
		try {
			// TODO What about null ? Can it really happen?
			Class ownerClass = owner.getClass();
			for (Method m : ownerClass.getMethods()) {
				if (m.getName().startsWith("get" + fieldName + "__DEPENDENCY_INFO")) {
					// System.out.println("DataDepEventHandler.extractStaticDependencyInfo()
					// Invoking " + m);
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

	// TODO References to Test Class and Test Method
	// THIS DOES NOT WORK!
	/*
	 * When a static initialization happens we get the wrong testMethod for some
	 * reason, so we use listenere instead [exec]
	 * ConflictDetection.beforeTestExecution() Starting test:
	 * crystal.client.ClientPreferencesTest.initializationError ID 14
	 * 
	 * [exec] DependencyCollector.main(...).new RunListener()
	 * {...}.testStarted()
	 * initializationError(crystal.client.ClientPreferencesTest) [exec]
	 * DependencyCollector.main(...).new RunListener() {...}.testFailure()
	 * initializationError(crystal.client.ClientPreferencesTest):
	 * crystal/client/ClientPreferences$DuplicateProjectNameException [exec]
	 * DependencyCollector.main(...).new RunListener() {...}.testFinished()
	 * initializationError(crystal.client.ClientPreferencesTest)
	 * 
	 */
	public void beforeTestExecution(String testClass, String testMethod) {

		DependencyInfo.CURRENT_TEST++;
		DependencyInfo.IN_CAPTURE = true;

		testNumToTestClass.put(DependencyInfo.CURRENT_TEST, testClass);
		testNumToMethod.put(DependencyInfo.CURRENT_TEST, testMethod);

	}

	public void afterTestExecution() {
		DependencyInfo.IN_CAPTURE = false;
		// Reset the DepInfo data which have a conflict ... Recursive ?!
		for (DependencyInfo d : conflicts) {
			// System.out.println("DataDepEventHandler.afterTestExecution()
			// Clearing conflict");
			d.clearConflict();
		}
	}

	public List<DataDependency> getDataDependencies() {
		// TODO Make a copy out of it ...
		return dataDependencies;
	}

}
