package example;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.StaticFieldDependency;
import utils.ClassLoaderHelper;

/**
 * The must run each in a separated JVM ! FIXME not sure how to achieve it with
 * failsafe !
 * 
 * @author gambi
 *
 */
public class JodaTimeIT {
	// (),
	// testLeapYearRulesConstruction(org.joda.time.chrono.TestGJChronology)

	private static boolean firstRun = true;

	@BeforeClass // - Integration tests run this multiple times anyway ...
	public static void preLoadAllClasses() throws ClassNotFoundException {

		if (firstRun) {
			System.out.println("JodaTimeIT.preLoadAllClasses()");
			ClassLoaderHelper cl = new ClassLoaderHelper();
			cl.getClassesForPackage("org.joda.time");
			firstRun = false;
		}
	}

	@Before
	public void setupWhitelist() {
		HeapWalker.resetAllState();
		//
		HeapWalker.clearWhitelist();
		HeapWalker.addToWhitelist("org.joda.time");
		//

	}

	@After
	public void clearAll() {
		HeapWalker.resetAllState();
	}

	private LinkedList<StaticFieldDependency> deps;
	private Collection<Entry<String, String>> depsData;

	// @Test
	// public void testNullStaticFieldValue() {
	//
	// System.out.println("JodaTimeIT.testNullStaticFieldValue() - STARTING ");
	// // FIXME: Note that this is parametric, not sure how we run it normally
	// // !!
	// (new TestGJChronology("name")).testPartialGetAsText();
	// deps =
	// HeapWalker.walkAndFindDependencies("org.joda.time.chrono.TestGJChronology",
	// "testPartialGetAsText");
	// //
	// (new TestGJChronology("name")).testLeapYearRulesConstruction();
	// deps =
	// HeapWalker.walkAndFindDependencies("org.joda.time.chrono.TestGJChronology",
	// "testLeapYearRulesConstruction");
	// }

	// @Test
	// public void testWriteAfterWriteOnOff() {
	// System.out.println("JodaTimeIT.testWriteAfterWriteOnOff()");
	// testWriteAfterWriteOn();
	// setupWhitelist();
	// testWriteAfterWriteOff();
	//
	// }
	//
	// @Test
	// public void testWriteAfterWriteOffOn() {
	// System.out.println("JodaTimeIT.testWriteAfterWriteOffOn()");
	// testWriteAfterWriteOff();
	// setupWhitelist();
	// testWriteAfterWriteOn();
	//
	// }
	//
	// @Test
	// public void testWriteAfterWriteOn() {
	// System.out.println("JodaTimeIT.testWriteAfterWriteOn()");
	// DependencyInfo.conflictsForWriteAfterWrite = true;
	// JUnitCore junit = new JUnitCore();
	// try {
	// Request r =
	// Request.method(org.joda.time.TestMutableDateTime_Constructors.class,
	// "testConstructor_nullDateTimeZone");
	// junit.run(r);
	// } catch (Throwable t) {
	// // Somehow this test fails
	// // t.printStackTrace();
	// }
	// deps =
	// HeapWalker.walkAndFindDependencies("org.joda.time.TestMutableDateTime_Constructors",
	// "testConstructor_nullDateTimeZone");
	//
	// System.out.println("Deps() " + deps.size());
	// //
	// try {
	// Request r = Request.method(org.joda.time.TestDateTime_Basics.class,
	// "testToString_String_Locale");
	// junit.run(r);
	// } catch (Throwable t) {
	// // Somehow this test fails
	// // t.printStackTrace();
	// }
	// deps =
	// HeapWalker.walkAndFindDependencies("org.joda.time.TestDateTime_Basics",
	// "testToString_String_Locale");
	//
	// //
	// System.out.println("JodaTimeIT.testMissingDeps() deps " + deps.size());
	//
	// }
	//
	// @Test
	// public void testWriteAfterWriteOff() {
	// System.out.println("JodaTimeIT.testWriteAfterWriteOff()");
	//
	// DependencyInfo.conflictsForWriteAfterWrite = false;
	// JUnitCore junit = new JUnitCore();
	// try {
	// Request r =
	// Request.method(org.joda.time.TestMutableDateTime_Constructors.class,
	// "testConstructor_nullDateTimeZone");
	// junit.run(r);
	// } catch (Throwable t) {
	// // Somehow this test fails
	// // t.printStackTrace();
	// }
	// deps =
	// HeapWalker.walkAndFindDependencies("org.joda.time.TestMutableDateTime_Constructors",
	// "testConstructor_nullDateTimeZone");
	//
	// System.out.println("JodaTimeIT.testMissingDeps() " + deps.size());
	// //
	// try {
	// Request r = Request.method(org.joda.time.TestDateTime_Basics.class,
	// "testToString_String_Locale");
	// junit.run(r);
	// } catch (Throwable t) {
	// // Somehow this test fails
	// // t.printStackTrace();
	// }
	// deps =
	// HeapWalker.walkAndFindDependencies("org.joda.time.TestDateTime_Basics",
	// "testToString_String_Locale");
	//
	// //
	// System.out.println("JodaTimeIT.testMissingDeps() deps " + deps.size());
	//
	// }
	@Test
	public void testFlakyTest() {
		JUnitCore junit = new JUnitCore();

		// // Now running the FLAKY test
		// try {
		// Request r =
		// Request.method(org.joda.time.TestDateMidnight_Basics.class,
		// "testWithZoneRetainFields_DateTimeZone");
		// junit.run(r);
		//
		// } catch (Throwable tr) {
		// Assert.fail();
		// }

		String testList = "testToInterval(org.joda.time.TestDateMidnight_Basics), testToString(org.joda.time.TestDateMidnight_Basics), testSerialization(org.joda.time.TestDateMidnight_Basics), testIsEqual(org.joda.time.TestDateMidnight_Basics), testIsBefore(org.joda.time.TestDateMidnight_Basics), testIsAfter(org.joda.time.TestDateMidnight_Basics), testToString_String_String(org.joda.time.TestDateMidnight_Basics), testToMutableDateTimeISO(org.joda.time.TestDateMidnight_Basics), testToMutableDateTime_DateTimeZone(org.joda.time.TestDateMidnight_Basics), testToMutableDateTime_Chronology(org.joda.time.TestDateMidnight_Basics), testToDate(org.joda.time.TestDateMidnight_Basics), testWithMillis_long(org.joda.time.TestDateMidnight_Basics), testWithDurationAdded_long_int(org.joda.time.TestDateMidnight_Basics), testWithDurationAdded_RD_int(org.joda.time.TestDateMidnight_Basics), testPlus_long(org.joda.time.TestDateMidnight_Basics), testPlus_RD(org.joda.time.TestDateMidnight_Basics), testMinus_long(org.joda.time.TestDateMidnight_Basics), testMinus_RD(org.joda.time.TestDateMidnight_Basics), testGet_DateTimeFieldType(org.joda.time.TestDateMidnight_Basics), testGet_DateTimeField(org.joda.time.TestDateMidnight_Basics), testEqualsHashCode(org.joda.time.TestDateMidnight_Basics), testCompareTo(org.joda.time.TestDateMidnight_Basics), testToInstant(org.joda.time.TestDateMidnight_Basics), testToDateTime(org.joda.time.TestDateMidnight_Basics), testToDateTimeISO(org.joda.time.TestDateMidnight_Basics), testToDateTime_DateTimeZone(org.joda.time.TestDateMidnight_Basics), testToDateTime_Chronology(org.joda.time.TestDateMidnight_Basics), testToMutableDateTime(org.joda.time.TestDateMidnight_Basics), testTest(org.joda.time.TestDateMidnight_Basics), testGetters(org.joda.time.TestDateMidnight_Basics), testWithers(org.joda.time.TestDateMidnight_Basics), testToString_String(org.joda.time.TestDateMidnight_Basics), testToString_DTFormatter(org.joda.time.TestDateMidnight_Basics), testToCalendar_Locale(org.joda.time.TestDateMidnight_Basics), testToGregorianCalendar(org.joda.time.TestDateMidnight_Basics), testToYearMonthDay(org.joda.time.TestDateMidnight_Basics), testToLocalDate(org.joda.time.TestDateMidnight_Basics), testWithChronology_Chronology(org.joda.time.TestDateMidnight_Basics), testWithZoneRetainFields_DateTimeZone(org.joda.time.TestDateMidnight_Basics)";
		//
		// testList = "testToInterval(org.joda.time.TestDateMidnight_Basics),
		// testToString(org.joda.time.TestDateMidnight_Basics)";

		String[] tests = testList.replaceAll(" ", "").split(",");

		// Do a binary search
		int half = tests.length / 2;

		// Running the first half
		for (int i = 0; i < half; i++) {
			// System.out.println("JodaTimeIT.testFlakyTest() " + t);
			String[] tokens = tests[i].replace("(", " ").replace(")", "").split(" ");
			System.out.println("JodaTimeIT.testFlakyTest() Running " + tokens[1] + "." + tokens[0]);
			try {
				Request r = Request.method(Class.forName(tokens[1]), tokens[0]);
				junit.run(r);

			} catch (Throwable tr) {
				// // Somehow this test fails
				// tr.printStackTrace();
				System.out.println("Failed " + tokens[1] + "." + tokens[0]);
				Assert.fail();
			}
		}

		// // Now running the FLAKY test
		// try {
		// Request r =
		// Request.method(org.joda.time.TestDateMidnight_Basics.class,
		// "testWithZoneRetainFields_DateTimeZone");
		// junit.run(r);
		// System.out.println("Passed in the first half ");
		// } catch (Throwable tr) {
		// // // Somehow this test fails
		// // tr.printStackTrace();
		// // Assert.fail();
		// System.out.println("Failed in the first half ");
		//
		// }

		System.out.println("JodaTimeIT.testFlakyTest() HALF ");

		// Running the second half
		for (int i = half; i < tests.length; i++) {
			// System.out.println("JodaTimeIT.testFlakyTest() " + t);
			String[] tokens = tests[i].replace("(", " ").replace(")", "").split(" ");
			System.out.println("JodaTimeIT.testFlakyTest() Running " + tokens[1] + "." + tokens[0]);
			try {
				Request r = Request.method(Class.forName(tokens[1]), tokens[0]);
				junit.run(r);
			} catch (Throwable tr) {
				// // Somehow this test fails
				// tr.printStackTrace();
				Assert.fail();
				System.out.println("Failed " + tokens[1] + "." + tokens[0]);
			}
		}

		// Now running the FLAKY test
		try {
			Request r = Request.method(org.joda.time.TestDateMidnight_Basics.class,
					"testWithZoneRetainFields_DateTimeZone");
			junit.run(r);
			// Assert.fail();
			System.out.println("Passed in the second half ");
		} catch (Throwable tr) {
			// // Somehow this test fails
			// tr.printStackTrace();
			// Assert.fail();
			// System.out.println("Failed " + tokens[1] + "." + tokens[0]);
			System.out.println("Failed in the second half ");
		}

		// deps =
		// HeapWalker.walkAndFindDependencies("org.joda.time.TestDateMidnight_Basics",
		// "testWithZoneRetainFields_DateTimeZone");

	}
}
