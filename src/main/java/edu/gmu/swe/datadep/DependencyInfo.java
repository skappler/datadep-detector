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
			if (IN_CAPTURE || ignored || conflict) {
				return;
			} else if (writeGen != 0 && writeGen != CURRENT_TEST_COUNT) {
				conflict = true;
				if (fields != null) {
					// Snag the value of the field only if there is at least a
					// SF associated to this object
					for (StaticField sf : fields) {
						if (sf != null) {
							if (xmlEl != null) {
								if (HeapWalker.testNumToTestClass.get(getWriteGen()) == null) {
									System.out.println("DependencyInfo.write() " + "FOUND NULL TEST ! " + getWriteGen()
											+ " " + HeapWalker.testNumToTestClass.size());
								} else {
									xmlEl.setAttribute("dependsOn", HeapWalker.testNumToTestClass.get(getWriteGen())
											+ "." + HeapWalker.testNumToMethod.get(getWriteGen()));
								}
								break; // We exit at the first non-null SF
										// object that we find
							}
						}
					}
					// "Bubble up" the conflict to all the associated SFs that
					// do not have already a conflict
					for (StaticField sf : fields) {
						if (sf != null) {
							if (!sf.isConflict()) {
								sf.markConflictAndSerialize(writeGen);
							}
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
		if (IN_CAPTURE || ignored || conflict) {
			return;
		} else if (writeGen != 0 && writeGen != CURRENT_TEST_COUNT) {
			conflict = true;
			// Snag the value of the field only if there is at least a SF
			// associated to this object
			if (fields != null) {
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
						break;

					}
				}

				// "Bubble up" the conflict to all the associated SFs that
				// do not have already a conflict
				for (StaticField sf : fields) {
					if (sf != null) {
						if (!sf.isConflict()) {
							sf.markConflictAndSerialize(writeGen);
						}
					}
				}
			}
		}
		// readGen = CURRENT_TEST_COUNT;
	}

	public static void write(Object obj) {
		if (obj instanceof MockedClass) {
			return;
		} else if (obj instanceof DependencyInstrumented) {
			((DependencyInstrumented) obj).getDEPENDENCY_INFO().write();
		} else if (obj instanceof DependencyInfo) {
			((DependencyInfo) obj).write();
		} else if (obj != null) {
			TagHelper.getOrInitTag(obj).write();
		}
	}

	public static void read(Object obj) {
		if (obj instanceof MockedClass) {
			return;
		} else if (obj instanceof DependencyInstrumented) {
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
