package de.unisaarland.instrumentation;

import java.io.Serializable;

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
	public boolean write() {
		// System.out.println("DependencyInfo.write() " + IN_CAPTURE + " " +
		// ignored + " " + conflict + " LW " + lastWrite
		// + " CURRENT TEST " + CURRENT_TEST);
		if (!IN_CAPTURE || ignored || conflict) {
			System.out.println("DependencyInfo.read() ALREADY CONFLICT !");
			return false;
		} else if (lastWrite != 0 && lastWrite != CURRENT_TEST) {
			conflict = true;
			lastWrite = CURRENT_TEST;
			return true;
		} else {
			lastWrite = CURRENT_TEST;
			return false;
		}

	}

	public boolean read() {
		// System.out.println("DependencyInfo.read() " + IN_CAPTURE + " " +
		// ignored + " " + conflict + " LR " + lastRead
		// + " CURRENT TEST " + CURRENT_TEST);
		if (!IN_CAPTURE || ignored || conflict) {
			System.out.println("DependencyInfo.read() ALREADY CONFLICT !");
			return false;
		} else if (lastWrite != 0 && lastWrite != CURRENT_TEST) {
			conflict = true;
			return true;
		} else {
			lastRead = CURRENT_TEST;
			return false;
		}
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

	public String toString() {
		return "DepInfo: LW: " + lastWrite + "; LR:" + lastRead + ";" + (isConflict() ? " >>>> CONFLICT " : "");
	}
}
