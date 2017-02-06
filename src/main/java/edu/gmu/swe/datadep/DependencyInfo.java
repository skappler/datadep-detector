package edu.gmu.swe.datadep;

import java.io.Serializable;

import org.jdom2.Element;

public final class DependencyInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static int CURRENT_TEST_COUNT = 1;

	private boolean ignored;
	private int crawledGen;
	private int writeGen;
	// private int readGen; // Will this overflow the memory ?!

	private boolean conflict;

	public Element xmlEl;
	StaticField[] fields;

	// Note that this is the value at the moment of the conflict detection !
	private String value;

	public DependencyInfo() {

	}

	private StackTraceElement[] ex;

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

	// Code under test
	private void accumulate() {
		String value = "";
		if (xmlEl.getAttribute("dependsOn") != null) {
			value = xmlEl.getAttribute("dependsOn").getValue();
			value = value + ",";
		}
		value = value + HeapWalker.testNumToTestClass.get(getWriteGen()) + "."
				+ HeapWalker.testNumToMethod.get(getWriteGen());
		xmlEl.setAttribute("dependsOn", value);
		//
		String type = "";
		if (xmlEl.getAttribute("dependsOnType") != null) {
			type = xmlEl.getAttribute("dependsOnType").getValue();
			type = type + ",";
		}
		type = type + "W";
		xmlEl.setAttribute("dependsOnType", type);
	}

	/*
	 * This updates the writeGen no matter what. That's why we use the finally
	 * clause
	 */
	public void write() {

		try {
			// By adding this we lose deps on repoKind ?
			if (IN_CAPTURE)
				return;
			if (ignored)
				return;
			if (conflict) {
				return;
			}

			if (writeGen != 0 && writeGen != CURRENT_TEST_COUNT) {
				conflict = true;
				// Snag the value of the field.
				if (fields != null) {
					for (StaticField sf : fields)
						if (sf != null) {
							// THIS IS NEVER TRIGGERED
							// System.out.println("DependencyInfo.write() " + sf
							// + " for " + xmlEl);
							if (xmlEl != null) {
								if (HeapWalker.testNumToTestClass.get(getWriteGen()) == null) {
									System.out.println("DependencyInfo.write() " + "FOUND NULL DI " + getWriteGen()
											+ " " + HeapWalker.testNumToTestClass.size());
								} else {
									xmlEl.setAttribute("dependsOn", HeapWalker.testNumToTestClass.get(getWriteGen())
											+ "." + HeapWalker.testNumToMethod.get(getWriteGen()));
								}
							}

							// FIXME What shall we do here ?!
							if (sf.isConflict()) {
								// TODO(gyori): The xmlEl is somehow null. When
								// can
								// this be null?
								// sf.markConflictAndSerialize(writeGen);
							} else {
								sf.markConflictAndSerialize(writeGen);
							}
						}
				}
			}
		} finally {
			// This must always run
			writeGen = CURRENT_TEST_COUNT;
		}
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
		if (conflict) {
			return;
		}
		if (writeGen != 0 && writeGen != CURRENT_TEST_COUNT) {
			conflict = true;
			// Snag the value of the field AT THIS TIME !
			if (fields != null) { /*
									 * could be null if only pointed to by
									 * ignored heap roots
									 */
				for (StaticField sf : fields) {
					if (sf != null) {
						// System.out.println("DependencyInfo.read() " + sf + "
						// for " + xmlEl + " writeGen " + writeGen);
						if (xmlEl != null) {
							if (HeapWalker.testNumToTestClass.get(getWriteGen()) == null) {
								System.out.println(
										"FOUND NULL DI " + getWriteGen() + " " + HeapWalker.testNumToTestClass.size());
							} else {
								xmlEl.setAttribute("dependsOn", HeapWalker.testNumToTestClass.get(getWriteGen()) + "."
										+ HeapWalker.testNumToMethod.get(getWriteGen()));
							}
						}

						if (sf.isConflict()) {
							// TODO(gyori): The xmlEl is somehow null. When can
							// this be null?
						} else
							sf.markConflictAndSerialize(writeGen);
					}
				}
			}
		}
		// readGen = CURRENT_TEST_COUNT;
	}

	public static void write(Object obj) {
		if (obj instanceof DependencyInstrumented) {
			((DependencyInstrumented) obj).getDEPENDENCY_INFO().write();
		} else if (obj instanceof DependencyInfo) {
			((DependencyInfo) obj).write();
		} else if (obj != null) {
			TagHelper.getOrInitTag(obj).write();
		}
	}

	public static void read(Object obj) {
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
