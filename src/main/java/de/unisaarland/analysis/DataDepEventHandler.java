package de.unisaarland.analysis;

import java.util.Arrays;

import de.unisaarland.instrumentation.DependencyInstrumented;

public class DataDepEventHandler {
	// TODO Check EventHandler and MyEventHandler of DReADS

	// Handle Test LifeCycle Events, store map tests to testID and such, update
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

	public void onInstanceFieldGet(Object value, //
			String fieldOwner, String fieldDesc, String fieldName, //
			Object owner, //
			boolean array, boolean primitive) {

		// if (owner instanceof DependencyInstrumented) {
		// System.out.println("Owner " + fieldOwner + " is
		// DependencyInstrumented with "
		// + ((DependencyInstrumented) owner).getDEPENDENCY_INFO());
		// }
		//
		// if (value instanceof DependencyInstrumented) {
		// System.out.println("Field " + fieldName + " is DependencyInstrumented
		// with "
		// + ((DependencyInstrumented) owner).getDEPENDENCY_INFO());
		// }

		// System.out.println(
		// "Read " + fieldName + " desc " + fieldDesc + " belongs " + fieldOwner
		// + " " + array + " " + primitive);

		if (owner != null) {
			if (Arrays.toString(owner.getClass().getInterfaces())
					.contains("de/unisaarland/instrumentation/DependencyInstrumented")) {
				System.out.println(
						"DataDepEventHandler.onInstanceFieldGet() " + fieldOwner + " is a DependencyInstrumented");
			}
		}
		// if (owner instanceof DependencyInstrumented) {
		// System.out.println("Owner " + fieldOwner + " is
		// DependencyInstrumented with");
		// }
	}

	// TODO Do we need old/new values ? Probably to work with null ?
	public void onInstanceFieldPut(Object value, //
			String fieldOwner, String fieldDesc, String fieldName, //
			Object owner, boolean array, boolean primitive) {
		// System.out.println(
		// "Wrote " + fieldName + " desc " + fieldDesc + " belongs " +
		// fieldOwner + " " + array + " " + primitive);

		// System.out.println("DataDepEventHandler.onInstanceFieldPut() owner "
		// + owner);
		// if (owner instanceof DependencyInstrumented) {
		// System.out.println("Owner " + fieldOwner + " is
		// DependencyInstrumented with "
		// + ((DependencyInstrumented) owner).getDEPENDENCY_INFO());
		// }
		//
		// if (value instanceof DependencyInstrumented) {
		// System.out.println("Field " + fieldName + " is DependencyInstrumented
		// with "
		// + ((DependencyInstrumented) owner).getDEPENDENCY_INFO());
		// }
	}
}
