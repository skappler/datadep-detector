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

	public static HashMap<Integer, String> testNumToMethod = new HashMap<Integer, String>();
	public static HashMap<Integer, String> testNumToTestClass = new HashMap<>();

	final private Set<DependencyInfo> conflicts;

	// TODO Not thread safe
	private DependencyInfo tempDep;
	private String tempFieldOwner;
	private String tempFieldName;

	final private List<DataDependency> dataDependencies;
	final private List<DataDependency> lastDataDependencies;

	private static DataDepEventHandler INSTANCE;

	// This result in a NPE
	// private static final Logger __log = Logging.getPackageInstance();

	private DataDepEventHandler() {
		conflicts = new HashSet<DependencyInfo>();
		dataDependencies = new ArrayList<DataDependency>();
		lastDataDependencies = new ArrayList<DataDependency>();
	}

	public static synchronized DataDepEventHandler instanceOf() {
		if (INSTANCE == null)
			INSTANCE = new DataDepEventHandler();
		return INSTANCE;
	}

	private String getCurrentTest() {
		return testNumToTestClass.get(DependencyInfo.CURRENT_TEST) + "."
				+ testNumToMethod.get(DependencyInfo.CURRENT_TEST);
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
				// __log.debug(getCurrentTest() + " Read primitive/String : " +
				// fieldOwner + "." + fieldName);
			}
		} else if (isArray) {
			if (fieldObject instanceof DependencyInstrumented) {
				// __log.debug(getCurrentTest() + " Read array: " + fieldOwner +
				// "." + fieldName);
				d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
				// Keep the reference to this object
				tempDep = d;
				tempFieldName = fieldName;
				tempFieldOwner = fieldOwner;
			}
		} else {
			// Regular object - What this is null ?
			if (fieldObject instanceof DependencyInstrumented) {
				// __log.debug(getCurrentTest() + " Read field: " + fieldOwner +
				// "." + fieldName);
				d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
			}
		}

		if (d != null) {
			int sourceTestID = d.getLastWrite();
			if (d.read()) {
				//
				conflicts.add(d);
				//
				DataDependency dataDependency = new DataDependency(fieldOwner, fieldName, //
						sourceTestID, DependencyInfo.CURRENT_TEST, //
						testNumToTestClass.get(sourceTestID) + "." + testNumToMethod.get(sourceTestID),
						testNumToTestClass.get(DependencyInfo.CURRENT_TEST) + "."
								+ testNumToMethod.get(DependencyInfo.CURRENT_TEST));
				dataDependencies.add(dataDependency);
				lastDataDependencies.add(dataDependency);
				// Print out the Conflict data
				// // __log.debug(dataDependency);
			}
		} else {
			// __log.debug("DataDepEventHandler.onStaticFieldGet() WARNING null
			// dep info");

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

		if (isPrimitive || fieldDesc.equals("Ljava/lang/String;")) {
			if (ownerObject instanceof DependencyInstrumented) {
				// __log.debug(getCurrentTest() + " Wrote primitive/String : " +
				// fieldOwner + "." + fieldName);
				d = extractDependencyInfo(ownerObject, fieldName);
			}
		} else if (isArray) {
			if (fieldObject instanceof DependencyInstrumented) {
				// __log.debug(getCurrentTest() + " Wrote array ref: " +
				// fieldOwner + "." + fieldName);
				d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
			}
		} else {
			// Regular object - What this is null ?
			if (fieldObject instanceof DependencyInstrumented) {
				// __log.debug(
				// getCurrentTest() + " Wrote field: " + fieldOwner + "." +
				// fieldName + " of desc " + fieldDesc);
				d = ((DependencyInstrumented) fieldObject).getDEPENDENCY_INFO();
			}
		}
		if (d != null) {
			// Before calling the write, otherwise we overwrite it !
			int sourceTestID = d.getLastWrite();

			if (d.write()) {
				//
				conflicts.add(d);
				//
				DataDependency dataDependency = new DataDependency(fieldOwner, fieldName, //
						sourceTestID, DependencyInfo.CURRENT_TEST, //
						testNumToTestClass.get(sourceTestID) + "." + testNumToMethod.get(sourceTestID),
						testNumToTestClass.get(DependencyInfo.CURRENT_TEST) + "."
								+ testNumToMethod.get(DependencyInfo.CURRENT_TEST));
				dataDependencies.add(dataDependency);
				lastDataDependencies.add(dataDependency); // Print out the
															// Conflict data
				// // __log.debug(dataDependency);
			}
		} else {
			// __log.debug("DataDepEventHandler.onStaticFieldGet() WARNING null
			// dep info");

		}
	}

	// Static fields have no owner. Owners are instances, static fields belong
	// to classes

	public void onStaticFieldGet(String fieldOwner, String fieldDesc, String fieldName,
			//
			Object owner, //
			//
			boolean isArray, boolean isPrimitive) {

		if (!DependencyInfo.IN_CAPTURE) {
			return;
		}

		// For some reason the GET guard cannot stop DependencyInfo objects ?!
		if (Instrumenter.isIgnoredClass(fieldDesc)) {
			return;
		}

		DependencyInfo d = extractStaticDependencyInfo(owner, fieldOwner, fieldName);
		if (d != null) {
			// __log.debug(getCurrentTest() + " Read " + fieldOwner + "." +
			// fieldName);
			int sourceTestID = d.getLastWrite();
			if (d.read()) {
				//
				conflicts.add(d);
				//

				DataDependency dataDependency = new DataDependency(fieldOwner, fieldName, //
						sourceTestID, DependencyInfo.CURRENT_TEST, //
						testNumToTestClass.get(sourceTestID) + "." + testNumToMethod.get(sourceTestID),
						testNumToTestClass.get(DependencyInfo.CURRENT_TEST) + "."
								+ testNumToMethod.get(DependencyInfo.CURRENT_TEST));
				dataDependencies.add(dataDependency);
				lastDataDependencies.add(dataDependency); // Print out the
															// Conflict data
				// // __log.debug(dataDependency);
			}

			// Propagate if array
			if (isArray) {
				// // __log.debug("DataDepEventHandler.onStaticFieldGet()
				// Propagate ");
				tempDep = d;
				tempFieldName = fieldName;
				tempFieldOwner = fieldOwner;
			}

		}
		// else {
		// __log.debug(
		// "DataDepEventHandler.onStaticFieldGet() WARNING null dep info for " +
		// fieldOwner + "/" + fieldName);
		// }

	}

	// Static fields have no owner. Owners are instances, static fields belong
	// to classes
	public void onStaticFieldPut(String fieldOwner, String fieldDesc, String fieldName,
			//
			Object owner, //
			// Object oldValue, Object newValue, // Probably they are not
			// required
			//
			boolean isArray, boolean isPrimitive) {

		if (!DependencyInfo.IN_CAPTURE) {
			return;
		}

		// For some reason the PUT guard cannot stop DependencyInfo objects ?!
		if (Instrumenter.isIgnoredClass(fieldDesc)) {
			return;
		}

		// // __log.debug("DataDepEventHandler.onStaticFieldPut() " +
		// fieldOwner + "." + fieldName + " from "
		// + oldValue + " to " + newValue);
		DependencyInfo d = extractStaticDependencyInfo(owner, fieldOwner, fieldName);
		if (d != null) {
			// __log.debug(getCurrentTest() + " Wrote " + fieldOwner + "." +
			// fieldName);
			int sourceTestID = d.getLastWrite();
			if (d.write()) {
				//
				conflicts.add(d);
				//
				DataDependency dataDependency = new DataDependency(fieldOwner, fieldName, //
						sourceTestID, DependencyInfo.CURRENT_TEST, //
						testNumToTestClass.get(sourceTestID) + "." + testNumToMethod.get(sourceTestID),
						testNumToTestClass.get(DependencyInfo.CURRENT_TEST) + "."
								+ testNumToMethod.get(DependencyInfo.CURRENT_TEST));
				dataDependencies.add(dataDependency);
				lastDataDependencies.add(dataDependency); // Print out the
															// Conflict data
				// // __log.debug(dataDependency);
			}
		}
		// else {
		// __log.debug(
		// "DataDepEventHandler.onStaticFieldPut() WARNING null dep info for " +
		// fieldOwner + "/" + fieldName);
		// }

	}

	// TODO will those triggers read/write inside arrays or of the array ref ?!
	// TODO No idea what arrayRef is compared to fieldValue...
	// FIXME Handle arrays of primitives !
	// Probably we need to store some state var about the array and eventually
	// on put/get check that write/read the array
	public void onArrayStore(int index, Object arrayRef, Object theArray) {

		if (!DependencyInfo.IN_CAPTURE) {
			return;
		} else {
			// TODO Safe ?
			// if (arrayRef.toString().startsWith("[Ljava.lang.String;")) {
			// // __log.debug("On Array of String Store : " + index + " " +
			// arrayRef);
			// } else if (arrayRef.toString().startsWith("[L")) {
			// // __log.debug("On Array of Object Store : " + index + " " +
			// arrayRef);
			// } else if (arrayRef.toString().startsWith("[[")) {
			// // Array of arrays ?
			// // __log.debug("On Array of Array Store : " + index + " " +
			// arrayRef);
			// } else {
			// // Array of primitives
			// // __log.debug("On Array of Primitives Store : " + index);
			// }

			if (tempDep != null) {
				// Store fieldOwner and
				// __log.debug(getCurrentTest() + " Propagate the Write " +
				// tempFieldOwner + "." + tempFieldName);
				int sourceTestID = tempDep.getLastWrite();
				if (tempDep.write()) {
					//
					conflicts.add(tempDep);
					//
					DataDependency dataDependency = new DataDependency(tempFieldOwner, tempFieldName, //
							sourceTestID, DependencyInfo.CURRENT_TEST, //
							testNumToTestClass.get(sourceTestID) + "." + testNumToMethod.get(sourceTestID),
							testNumToTestClass.get(DependencyInfo.CURRENT_TEST) + "."
									+ testNumToMethod.get(DependencyInfo.CURRENT_TEST));
					dataDependencies.add(dataDependency);
					lastDataDependencies.add(dataDependency); // Print out the
																// Conflict data
					// // __log.debug(dataDependency);
				}
				// Reset temp
				tempDep = null;
				tempFieldName = null;
				tempFieldOwner = null;
			}
		}

	}

	public void onArrayLoad(int index, Object arrayRef, Object fieldObject) {

		if (!DependencyInfo.IN_CAPTURE) {
			return;
		} else {
			// // Safe ?!
			// if (arrayRef.toString().startsWith("[Ljava.lang.String;")) {
			// // __log.debug("On Array of String Load : " + index + " " +
			// arrayRef);
			// } else if (arrayRef.toString().startsWith("[L")) {
			// // __log.debug("On Array of Object Load : " + index + " " +
			// arrayRef);
			// } else if (arrayRef.toString().startsWith("[[")) {
			// // Array of arrays ?
			// // __log.debug("On Array of Array Load : " + index + " " +
			// arrayRef);
			// } else {
			// // Array of primitives
			// // __log.debug("On Array of Primitives Load : " + index + " "
			// + arrayRef);
			// }
			// At this point the array was already read, and we free the
			// temporary data
			if (tempDep != null) {
				// // __log.debug("Stop propagating to the array !");
				// The pattern is always access array -> store an element. A
				// Reset temp
				tempDep = null;
				tempFieldName = null;
				tempFieldOwner = null;
			}
		}

	}

	private static DependencyInfo extractDependencyInfo(Object ownerObject, String fieldName) {
		try {
			for (Method m : ownerObject.getClass().getMethods()) {
				if (m.getName().startsWith("get" + fieldName + "__DEPENDENCY_INFO")) {
					// //
					// __log.debug("DataDepEventHandler.extractDependencyInfo()
					// invoking " + m);
					return (DependencyInfo) m.invoke(ownerObject);
				}
			}
		} catch (Exception e) {
			// __log.debug("DataDepEventHandler.extractDependencyInfo() FAILED
			// with " + e);
			e.printStackTrace();
		}
		// This should never happen ....
		// __log.debug("DataDepEventHandler.extractDependencyInfo() Cannot find
		// accessor method for " + fieldName);
		return null;

	}

	private static DependencyInfo extractStaticDependencyInfo(Object owner, String fieldOwner, String fieldName) {

		if (owner == null) {
			// __log.debug("DataDepEventHandler.extractStaticDependencyInfo()
			// Null Owner");
			return null;
		}
		try {
			// TODO What about null ? Can it really happen?
			Class ownerClass = owner.getClass();
			for (Method m : ownerClass.getMethods()) {
				if (m.getName().startsWith("get" + fieldName + "__DEPENDENCY_INFO")) {
					// //
					// __log.debug("DataDepEventHandler.extractStaticDependencyInfo()
					// Invoking " + m);
					return (DependencyInfo) m.invoke(null, new Object[0]);
				}
			}
			// __log.debug("DataDepEventHandler.extractStaticDependencyInfo()
			// WARNING Cannot find method " + "get"
			// + fieldName + "__DEPENDENCY_INFO");
			// TODO debug:
			for (Method m : ownerClass.getMethods()) {
				// __log.debug("DataDepEventHandler.extractStaticDependencyInfo()
				// -- " + m);
			}

		} catch (Exception e) {
			// __log.debug("DataDepEventHandler.extractStaticDependencyInfo()
			// FAILED with " + e);
			e.printStackTrace();
		}
		// This should never happen ....
		return null;

	}

	/*
	 * EVENT LIFE CYCLE !
	 */
	/**
	 * 
	 * @param testClass
	 * @param testMethod
	 */
	public void beforeTestExecution(String testClass, String testMethod) {

		DependencyInfo.CURRENT_TEST++;
		DependencyInfo.IN_CAPTURE = true;

		testNumToTestClass.put(DependencyInfo.CURRENT_TEST, testClass);
		testNumToMethod.put(DependencyInfo.CURRENT_TEST, testMethod);

		lastDataDependencies.clear();

	}

	public void afterTestExecution() {
		DependencyInfo.IN_CAPTURE = false;
		for (DependencyInfo d : conflicts) {
			d.clearConflict();
		}
		conflicts.clear();
	}

	// Why a list and not a SET? How is it even possible that the same elements
	// appear over and over ?!
	public List<DataDependency> getDataDependencies() {
		// TODO Make a copy out of it ...
		return dataDependencies;
	}

	public List<DataDependency> getLastDataDependencies() {
		return lastDataDependencies;
	}

}
