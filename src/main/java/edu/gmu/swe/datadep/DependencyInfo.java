package edu.gmu.swe.datadep;

import java.io.Serializable;

import org.jdom2.Element;

// using the code below:
//MockingDetails md = org.mockito.Mockito.mockingDetails(obj);
// if (md.isMock() || md.isSpy()) {
// return;
// }
// Results in :
//#
//# A fatal error has been detected by the Java Runtime Environment:
//#
//#  SIGSEGV (0xb) at pc=0x0000000000000000, pid=10829, tid=4867
//#
//same goes when using the getClass().toString() code
// 
// TODO: Try to categorize dependencies in read-after-write, write-after-read, write-after-write. Enable the READ_GEN Stuff ! Pay attention to memory overflow
public final class DependencyInfo implements Serializable {

	public static boolean conflictsForWriteAfterWrite = false;
	public static boolean storeXMLState = false;

	private static final long serialVersionUID = 1L;

	public static int CURRENT_TEST_COUNT = 1;

	private boolean ignored;
	private int crawledGen;
	private int writeGen;
	// private int readGen; // Will this overflow the memory ?!
	// private String value;

	private boolean conflict;

	public Element xmlEl;
	StaticField[] fields;


	public DependencyInfo() {

	}

	// private StackTraceElement[] ex;

	public int getCrawledGen() {
		return crawledGen;
	}

	public void setCrawledGen() {
		this.crawledGen = CURRENT_TEST_COUNT;
	}

	public boolean isConflict() {
		return conflict;
	}

	public int getWriteGen() {
		return writeGen;
	}

	// Not sure about this, especially for read after write.
	///
	public void write() {
		// TODO FIXME
		// We report also the Write after Write as conflicts
		// Note the use of read() is a TRICK !
		// read();
		//
		// R -> W1, W2 === W2, R -> W1
		// Can we capture the write after write ?
		// TODO Not sure why with readGen everything breaks
		if (conflictsForWriteAfterWrite && writeGen != CURRENT_TEST_COUNT)
		// && readGen != CURRENT_TEST_COUNT
		// && writeGen == readGen)
		{
			// TODO Must be the same test ?!
			// This is a write after write. However, shall we consider it only
			// if there was a read before, but in that case, this would be
			// already a write after read ?

			// If there was a read before. Is crawler gen ok os we need to use
			// readGen ?
			conflict = true;
			// Snag the value of the field.
			if (fields != null) {
				for (StaticField sf : fields)
					if (sf != null) {

						if (storeXMLState && xmlEl != null) {
							if (HeapWalker.testNumToTestClass.get(getWriteGen()) == null) {
								System.out.println("DependencyInfo.write() " + "FOUND NULL DI " + getWriteGen() + " "
										+ HeapWalker.testNumToTestClass.size());
							} else {
								xmlEl.setAttribute("dependsOn", HeapWalker.testNumToTestClass.get(getWriteGen()) + "."
										+ HeapWalker.testNumToMethod.get(getWriteGen()));
							}
						}

						// FIXME What shall we do here ?!
						if (sf.isConflict()) {
							// TODO(gyori): The xmlEl is somehow null. When can
							// this be null?
						} else {
							sf.markConflictAndSerialize(writeGen);
						}
					}
			}
		}
		writeGen = CURRENT_TEST_COUNT;

	}

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public static boolean IN_CAPTURE = false;

	public void read() {
		if (IN_CAPTURE)
			return;
		if (ignored)
			return;
		if (conflict)
			return;
		if (writeGen != 0 && writeGen != CURRENT_TEST_COUNT) {
			conflict = true;
			// Snag the value of the field.
			if (fields != null) // could be null if only pointed to by ignored
								// heap roots
				for (StaticField sf : fields)
					if (sf != null) {

						if (storeXMLState && xmlEl != null) {
							if (HeapWalker.testNumToTestClass.get(getWriteGen()) == null)
								System.out.println(
										"FOUND NULL DI " + getWriteGen() + " " + HeapWalker.testNumToTestClass.size());
							else
								xmlEl.setAttribute("dependsOn", HeapWalker.testNumToTestClass.get(getWriteGen()) + "."
										+ HeapWalker.testNumToMethod.get(getWriteGen()));
						}

						if (sf.isConflict()) {
							// TODO(gyori): The xmlEl is somehow null. When can
							// this be null?
						} else
							sf.markConflictAndSerialize(writeGen);
					}
		}
		// readGen = CURRENT_TEST_COUNT;
	}

	public static void write(Object obj) {

		if (obj instanceof MockedClass) {
			return;
		}

		if (obj instanceof DependencyInstrumented) {
			((DependencyInstrumented) obj).getDEPENDENCY_INFO().write();
		} else if (obj instanceof DependencyInfo) {
			((DependencyInfo) obj).write();
		} else if (obj != null) {
			TagHelper.getOrInitTag(obj).write();
		}
	}

	// This call for a mocked object results in a chain, we cannot avoid
	// this in mockito
	// since mockito uses CGLib which extends classes, so we get their
	// interfaces... as well

	public static void read(Object obj) {
		if (obj instanceof MockedClass) {
			return;
		}
		if (obj instanceof DependencyInstrumented) {
			((DependencyInstrumented) obj).getDEPENDENCY_INFO().read();
		} else if (obj instanceof DependencyInfo) {
			((DependencyInfo) obj).read();
		} else if (obj != null) {
			TagHelper.getOrInitTag(obj).read();
		}
	}

	public void clearConflict() {
		this.conflict = false;
	}

	public void clearSFs() {
		if (fields != null)
			for (int i = 0; i < fields.length; i++)
				fields[i] = null;
	}
}
