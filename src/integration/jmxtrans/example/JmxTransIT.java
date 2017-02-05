package example;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

import com.googlecode.jmxtrans.model.MergingTests;
import com.googlecode.jmxtrans.model.output.OpenTSDBGenericWriterTests;

import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.StaticFieldDependency;

public class JmxTransIT {

	@BeforeClass
	public static void setupWhitelist() {

		System.out.println("\n\n\n JmxTransIT.setupWhitelist() \n\n\n");

		HeapWalker.clearWhitelist();
		String[] whiteList = new String[] { "com.googlecode.jmxtrans", "com.googlecode.jmxtrans.classloader",
				"com.googlecode.jmxtrans.cli", "com.googlecode.jmxtrans.connections", "com.googlecode.jmxtrans.example",
				"com.googlecode.jmxtrans.exceptions", "com.googlecode.jmxtrans.guice", "com.googlecode.jmxtrans.jmx",
				"com.googlecode.jmxtrans.jobs", "com.googlecode.jmxtrans.model", "com.googlecode.jmxtrans.model.naming",
				"com.googlecode.jmxtrans.model.output", "com.googlecode.jmxtrans.model.output",
				"com.googlecode.jmxtrans.model.results", "com.googlecode.jmxtrans.monitoring",
				"com.googlecode.jmxtrans.util" };
		for (String wl : whiteList)
			HeapWalker.addToWhitelist(wl);

		//
		HeapWalker.addToWhitelist("crystal");
		HeapWalker.addToWhitelist("crystal.model");
		//
		System.out.println("Manually forcing whitelist");
	}

	private LinkedList<StaticFieldDependency> deps;
	private Collection<Entry<String, String>> depsData;

	@Test
	public void testMissingDeps() {

		System.setProperty(HeapWalker.STORE_XML_STATE, "true");
		System.setProperty(HeapWalker.USE_WAW, "true");

		// try {
		// OpenTSDBGenericWriterTests test = new OpenTSDBGenericWriterTests();
		// test.testMergedTypeNameValues1();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//

		JUnitCore junit = new JUnitCore();

		try {
			// This invoked System.exit(1) why is NOT happening !!!
			System.out.println(
					"JmxTransIT.testMissingDeps() OpenTSDBGenericWriterTests.data " + OpenTSDBGenericWriterTests.data);
			Request r = Request.method(OpenTSDBGenericWriterTests.class, "testMergedTypeNameValues1");
			junit.run(r);
			System.out.println(
					"JmxTransIT.testMissingDeps() OpenTSDBGenericWriterTests.data " + OpenTSDBGenericWriterTests.data);
		} catch (Throwable t) {
			// Somehow this test fails
			t.printStackTrace();
		}
		deps = HeapWalker.walkAndFindDependencies("com.googlecode.jmxtrans.model.output.OpenTSDBGenericWriterTests",
				"testMergedTypeNameValues1");
		Assert.assertEquals(0, deps.size());

		try {
			Request r = Request.method(OpenTSDBGenericWriterTests.class, "testMergedTypeNameValues2");
			junit.run(r);
			System.out.println("JmxTransIT.testMissingDeps() End run test: " + r);
		} catch (Throwable t) {
			// Somehow this test fails
			t.printStackTrace();
		}
		deps = HeapWalker.walkAndFindDependencies("com.googlecode.jmxtrans.model.output.OpenTSDBGenericWriterTests",
				"testMergedTypeNameValues2");
		//
		Assert.assertTrue(deps.size() > 0);
	}

	//
	@Test
	public void testMergingTests() {

		JUnitCore junit = new JUnitCore();

		try {
			Request r = Request.method(MergingTests.class, "mergeAlreadyExistingServerDoesNotModifyList");
			junit.run(r);
			System.out.println("JmxTransIT.testMergingTests() End run test: " + r);

		} catch (Throwable t) {
			// Somehow this test fails
			t.printStackTrace();
		}
		deps = HeapWalker.walkAndFindDependencies("com.googlecode.jmxtrans.model.MergingTests",
				"mergeAlreadyExistingServerDoesNotModifyList");

		Assert.assertEquals(0, deps.size());

		try {
			Request r = Request.method(MergingTests.class, "sameServerWithTwoDifferentQueriesMergesQueries");
			junit.run(r);
			System.out.println("JmxTransIT.testMergingTests() End run test: " + r);
		} catch (Throwable t) {
			// Somehow this test fails
			t.printStackTrace();
		}
		deps = HeapWalker.walkAndFindDependencies("com.googlecode.jmxtrans.model.MergingTests",
				"sameServerWithTwoDifferentQueriesMergesQueries");
		//
		System.out.println("JmxTransIT.testMergingTests() DEPS " + deps);
	}

}
