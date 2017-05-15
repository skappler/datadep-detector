package edu.gmu.swe.datadep;

import java.io.Serializable;

import org.jdom2.Element;

public final class DependencyInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static int CURRENT_TEST_COUNT = 1;

	private boolean ignored;

	private boolean logMe;

	private int crawledGen;
	private int writeGen;
	// private int readGen; // Will this overflow the memory ?!

	private boolean conflict;

	//
	private Element xmlEl;
	StaticField[] fields;

	public void setXmlEl(Element xmlEl) {
		// if (logMe) {
		// System.out.println(this.logMeName + " set XML from " + this.xmlEl + "
		// to " + xmlEl);
		// }
		this.xmlEl = xmlEl;
	}

	public Element getXmlEl() {
		return xmlEl;
	}

	// Note that this is the value at the moment of the conflict detection !
	private String value;

	public DependencyInfo() {

	}

	private StackTraceElement[] ex;

	private String logMeName;

	public int getCrawledGen() {
		return crawledGen;
	}

	public void setCrawledGen() {
		this.crawledGen = CURRENT_TEST_COUNT;
	}

	public boolean isConflict() {
		if (logMe && conflict) {
			System.out.println(this.logMeName + " is in Conflict !");
		}
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

		if (logMe) {
			System.out.println(
					this.logMeName + " write() Before write " + writeGen + " after write " + CURRENT_TEST_COUNT);

			/// This is the only way to really debug this code
			// Exception ex = new RuntimeException();
			// ex.printStackTrace();
		}

		try {
			// By adding this we lose deps on repoKind ?
			if (IN_CAPTURE || ignored || conflict) {
				return;
			} else if (writeGen != 0 && writeGen != CURRENT_TEST_COUNT) {
				conflict = true;
				if (logMe) {
					System.out.println(">>>>> " + this.logMeName + " write(): Conflict ! ");
				}
				if (xmlEl != null) {
					if (HeapWalker.testNumToTestClass.get(getWriteGen()) == null) {
						System.out.println("DependencyInfo.write() " + "FOUND NULL DI " + getWriteGen() + " "
								+ HeapWalker.testNumToTestClass.size());
					} else {
						xmlEl.setAttribute("dependsOn", HeapWalker.testNumToTestClass.get(getWriteGen()) + "."
								+ HeapWalker.testNumToMethod.get(getWriteGen()));
						// Note add vs set
						xmlEl.setAttribute("setBy", "DependencyInfo-Write");

					}
				}

				// Snag the value of the field.
				if (fields != null) {
					// Snag the value of the field only if there is at least a
					// SF associated to this object
					for (StaticField sf : fields) {
						if (sf != null) {
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

	public void logMe(String name) {
		this.logMe = true;
		this.logMeName = name;
	}

	public static boolean IN_CAPTURE = false;

	// TODO read and write shares a lot of code, extract to common private
	// method !!

	public void read() {
		if (logMe) {
			System.out.println(this.logMeName + " read(): last written " + writeGen);
		}
		if (IN_CAPTURE || ignored || conflict) {
			return;
		} else if (writeGen != 0 && writeGen != CURRENT_TEST_COUNT) {
			conflict = true;
			if (logMe) {
				System.out.println(">>>>> " + this.logMeName + " read(): Conflict ! ");
			}
			// Why this should be repeated for all the sf ?
			if (xmlEl != null) {
				if (HeapWalker.testNumToTestClass.get(getWriteGen()) == null) {
					System.out.println("FOUND NULL DI " + getWriteGen() + " " + HeapWalker.testNumToTestClass.size());
				} else {
					xmlEl.setAttribute("dependsOn", HeapWalker.testNumToTestClass.get(getWriteGen()) + "."
							+ HeapWalker.testNumToMethod.get(getWriteGen()));
					xmlEl.setAttribute("setBy", "DependencyInfo-Read-" + writeGen);
				}
			}

			// Snag the value of the static fields at the time of the conflict,
			// possibly this override previous values, which is bad:
			// write a <-foo (conflict a)
			// write a.1 <- boo (rewrite the entire a ?!)
			// Do we really need the serialization after all ?!
			if (fields != null) {
				for (StaticField sf : fields) {
					if (sf != null) {
						if (sf.isConflict()) {
							// TODO(gyori): The xmlEl is somehow null. When can
							// this be null?
						} else {
							// Write the current state of this particular SF at
							// this time
							// System.out.println("DependencyInfo.read()
							// Serialize
							// Static Field " + sf.field.getName() + " - "
							// + System.identityHashCode(sf));
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
		if (logMe) {
			System.out.println(">>>>> " + this.logMeName + " clearConflict()");
		}
		this.conflict = false;
	}

	public void clearSFs() {
		if (fields != null)
			for (int i = 0; i < fields.length; i++)
				fields[i] = null;
	}

	public String printMe() {
		if (logMe)
			return this.logMeName;
		else
			return "";
	}
}
