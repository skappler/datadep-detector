package example;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import crystal.model.DataSourceTest;
import crystal.model.DataSourceTestAlessio;
import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.StaticFieldDependency;

/**
 * @author gambi
 *
 */
public class Crystal_4_IT extends AbstractCrystalIT {

	@Test
	public void testMultipleReads() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		// force and ignore static initializers
		// HeapWalker.walkAndFindDependencies("INIT", "INIT");

		executeTest(DataSourceTestAlessio.class, "testSetField");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		executeTest(DataSourceTestAlessio.class, "testReadALL");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testReadALL");
		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");

		executeTest(DataSourceTestAlessio.class, "testReadALL2");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testReadALL2");
		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");

		executeTest(DataSourceTestAlessio.class, "testReadALL3");

		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testReadALL3");
		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__hide");

		executeTest(DataSourceTestAlessio.class, "testReadALL4");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testReadALL4");
		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__hide");

		executeTest(DataSourceTestAlessio.class, "testSetCloneString");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCloneString");
		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");

		executeTest(DataSourceTestAlessio.class, "testSetKind");

		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");
		// dependsOn="crystal.model.DataSourceTestAlessio.testSetCloneString" on
		// __cloneString
		// dependsOn="crystal.model.DataSourceTestAlessio.testSetField" on
		// __kind,

		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data"); // NOT
																														// SURE
																														// ABOUT
																														// THIS
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetCloneString", "__cloneString");
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
	}

}