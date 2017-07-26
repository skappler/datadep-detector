package example;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import crystal.model.LocalStateResultTest;
import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.StaticFieldDependency;

/**
 * @author gambi
 *
 */
public class Crystal_3_IT extends AbstractCrystalIT {

	@Test
	public void testMapd() {
		// crystal.model.LocalStateResultTest.testToString
		// crystal.model.LocalStateResultTest.testGetLastAction/
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		String testName = "testToString";

		//
		executeTest(LocalStateResultTest.class, testName);

		// This writes to several part
		deps = HeapWalker.walkAndFindDependencies("crystal.model.LocalStateResultTest", testName);
		depsData = extractAllDepValues(deps);
		Assert.assertTrue(depsData.isEmpty());

		//
		testName = "testGetLastAction";
		executeTest(LocalStateResultTest.class, testName);
		deps = HeapWalker.walkAndFindDependencies("crystal.model.LocalStateResultTest", testName);
		depsData = extractAllDepValues(deps);
		System.out.println("Crystal_3_IT.testMapd() depsData " + depsData);

		//
		testName = "testGetName";
		executeTest(LocalStateResultTest.class, testName);
		deps = HeapWalker.walkAndFindDependencies("crystal.model.LocalStateResultTest", testName);
		depsData = extractAllDepValues(deps);
		System.out.println("Crystal_3_IT.testMapd() depsData " + depsData);

		//
		testName = "testGetNoErrorMessage";
		executeTest(LocalStateResultTest.class, testName);
		deps = HeapWalker.walkAndFindDependencies("crystal.model.LocalStateResultTest", testName);
		depsData = extractAllDepValues(deps);
		System.out.println("Crystal_3_IT.testMapd() depsData " + depsData);

		//
		testName = "testGetLastErrorMessage";
		executeTest(LocalStateResultTest.class, testName);
		deps = HeapWalker.walkAndFindDependencies("crystal.model.LocalStateResultTest", testName);
		depsData = extractAllDepValues(deps);
		System.out.println("Crystal_3_IT.testMapd() depsData " + depsData);

		//
		testName = "testGetLocalState";
		executeTest(LocalStateResultTest.class, testName);
		deps = HeapWalker.walkAndFindDependencies("crystal.model.LocalStateResultTest", testName);
		depsData = extractAllDepValues(deps);
		System.out.println("Crystal_3_IT.testMapd() depsData " + depsData);

		//
		testName = "testGetLastLocalState";
		executeTest(LocalStateResultTest.class, testName);
		deps = HeapWalker.walkAndFindDependencies("crystal.model.LocalStateResultTest", testName);
		depsData = extractAllDepValues(deps);
		System.out.println("Crystal_3_IT.testMapd() depsData " + depsData);

		//
		testName = "testErrorActionNameConstructor";
		executeTest(LocalStateResultTest.class, testName);
		deps = HeapWalker.walkAndFindDependencies("crystal.model.LocalStateResultTest", testName);
		depsData = extractAllDepValues(deps);
		System.out.println("Crystal_3_IT.testMapd() depsData " + depsData);

		//
		testName = "testGetAction";
		executeTest(LocalStateResultTest.class, testName);
		deps = HeapWalker.walkAndFindDependencies("crystal.model.LocalStateResultTest", testName);
		depsData = extractAllDepValues(deps);
		System.out.println("Crystal_3_IT.testMapd() depsData " + depsData);

		//
		testName = "testStrangeActionName";
		executeTest(LocalStateResultTest.class, testName);
		deps = HeapWalker.walkAndFindDependencies("crystal.model.LocalStateResultTest", testName);
		depsData = extractAllDepValues(deps);
		System.out.println("Crystal_3_IT.testMapd() depsData " + depsData);

	}
	//
	// LinkedList<StaticFieldDependency> deps;
	// Collection<Entry<String, String>> depsData;
	//
	// executeTest(DataSourceTestAlessio.class, "testSetField");
	// deps =
	// HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio",
	// "testSetField");
	// depsData =
	// extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(),
	// deps);
	//
	// Assert.assertTrue(depsData.size() == 0);
	//
	// executeTest(DataSourceTestAlessio.class, "testReadALL");
	// deps =
	// HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio",
	// "readALL");
	// depsData =
	// extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(),
	// deps);
	//
	// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
	// "crystal.model.DataSourceTestAlessio.data");
	// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
	// "__shortName");
	// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
	// "__cloneString");
	// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
	// "__parent");
	// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
	// "__repoKind");
	//
	// executeTest(DataSourceTestAlessio.class, "testSetKind");
	// deps =
	// HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio",
	// "testSetKind");
	// depsData =
	// extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(),
	// deps);
	//
	// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
	// "crystal.model.DataSourceTestAlessio.data");
	// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
	// "__cloneString");
	// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
	// "__repoKind");
	//
	// }

}