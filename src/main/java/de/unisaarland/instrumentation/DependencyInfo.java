package de.unisaarland.instrumentation;

import java.io.Serializable;

import ch.usi.dag.disl.util.Logging;
import ch.usi.dag.util.logging.Logger;

/**
 * This class implements the Taint object
 * 
 * @author gambi
 *
 */
public final class DependencyInfo implements Serializable {

	// private static final Logger __log = Logging.getPackageInstance();
	private static final long serialVersionUID = 1L;

	private boolean ignored; // TODO use a guard in DiSL insted
	private boolean conflict; // TODO Compute this dynamically

	// State vars
	private int lastWrite;
	private int lastRead;

	public DependencyInfo() {
		// System.out.println("DependencyInfo.DependencyInfo()");
	}

	public int getLastWrite() {
		return lastWrite;
	}

	public int getLastRead() {
		return lastRead;
	}

	public boolean isConflict() {
		return conflict;
	}

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public static boolean IN_CAPTURE = false;// Not sure if this is better here
	// or inside the DiSL Code...

	// TODO I would like to change this, however, atm JB instrumentation is
	// configured to invoke write/read methods without parameters.
	public static int CURRENT_TEST = 0;

	/**
	 * This method records that the owner of this was written during test. <br/>
	 * TODO Question: Can we really pass int values around? Will they
	 * instrumented as well creating a cycle?
	 * 
	 * @param testID
	 */
	public void write() {
		System.out.println("DependencyInfo.write() " + CURRENT_TEST);
		// TODO Compute conflict dynamically
		// TODO IN_CAPTURE remove from here and place this in event handler
		// or something ?
		// TODO ignored remove from here and place this in event handler or
		// something ?
		if (IN_CAPTURE || ignored || conflict) {
			return;
		} else if (lastWrite != 0 && lastWrite != CURRENT_TEST) {

			conflict = true;

			// TODO What if TestID is not available ?

			// TODO What we do with complex objects? Do we propagate the info
			// about this new write down there?
			// Isn't already included in the write(testID) stuff ?
			// TODO Snag the value of the field. - We do not need this but we
			// might need to propagate the values and serialize instead ?
			// if (fields != null) {
			// // Snag the value of the field only if there is at least a
			// // SF associated to this object
			// for (StaticField sf : fields) {
			// if (sf != null) {
			// // FIXME What shall we do here ?!
			// if (sf.isConflict()) {
			// // TODO(gyori): The xmlEl is somehow null. When
			// // can
			// // this be null?
			// // sf.markConflictAndSerialize(writeGen);
			// } else {
			// sf.markConflictAndSerialize(lastWrite);
			// }
			// }
			// }
			// }
		}
		lastWrite = CURRENT_TEST;
	}

	public void read() {
		System.out.println("DependencyInfo.read() " + CURRENT_TEST);
		if (IN_CAPTURE || ignored || conflict) {
			return;
		} else if (lastWrite != 0 && lastWrite != CURRENT_TEST) {
			conflict = true;

			// TODO What if TestID is not available ?

			// // Snag the value of the static fields AT THIS TIME !
			// if (fields != null) { /*
			// * could be null if only pointed to by
			// * ignored heap roots
			// */
			// for (StaticField sf : fields) {
			// if (sf != null) {
			// if (sf.isConflict()) {
			// // TODO(gyori): The xmlEl is somehow null. When can
			// // this be null?
			// } else {
			// // Write the current state of this particular SF at
			// // this time
			//
			// sf.markConflictAndSerialize(lastWrite);
			// }
			// }
			// }
			// }
		}
		lastRead = CURRENT_TEST;
	}

	// Not really sure why there is those accesses to the tags
	// Dynamic Context is managed by DiSL
	// public static void write(Object obj) {
	// if (obj instanceof MockedClass) {
	// return;
	// } else if (obj instanceof DependencyInstrumented) {
	// ((DependencyInstrumented) obj).getDEPENDENCY_INFO().write();
	// } else if (obj instanceof DependencyInfo) {
	// ((DependencyInfo) obj).write();
	// } else if (obj != null) {
	// TagHelper.getOrInitTag(obj).write();
	// }
	// }
	//
	// public static void read(Object obj) {
	// if (obj instanceof MockedClass) {
	// return;
	// } else if (obj instanceof DependencyInstrumented) {
	// ((DependencyInstrumented) obj).getDEPENDENCY_INFO().read();
	// } else if (obj instanceof DependencyInfo) {
	// ((DependencyInfo) obj).read();
	// } else if (obj != null) {
	// TagHelper.getOrInitTag(obj).read();
	// }
	// }

	public void clearConflict() {
		this.conflict = false;
	}

	// public void clearSFs() {
	// if (fields != null)
	// for (int i = 0; i < fields.length; i++)
	// fields[i] = null;
	// }
}
