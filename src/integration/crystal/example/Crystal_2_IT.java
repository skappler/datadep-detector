package example;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import crystal.model.DataSourceTest;
import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.StaticFieldDependency;

/**
 * @author gambi
 *
 */
public class Crystal_2_IT extends AbstractCrystalIT {

	private Collection<Entry<String, String>> runTestAndGetDeps(Class testClass, String testMethod) {
		executeTest(testClass, testMethod);
		LinkedList<StaticFieldDependency> deps = HeapWalker.walkAndFindDependencies(testClass.getName(), testMethod);
		Collection<Entry<String, String>> dataDeps = extractAllDepValues(deps);
		//
		System.out.println(dataDeps);
		//
		return dataDeps;
	}

	@Test
	public void testCrystal() {

		Collection<Entry<String, String>> depsData;

		depsData = runTestAndGetDeps(DataSourceTest.class, "testSetField");
		Assert.assertTrue(depsData.size() == 0);
		//
		depsData = runTestAndGetDeps(DataSourceTest.class, "testSetRemoteCmd");
		has(depsData, "crystal.model.DataSourceTest.testSetField", "crystal.model.DataSourceTest.data");

		depsData = runTestAndGetDeps(DataSourceTest.class, "testSetCompileCommand");
		has(depsData, "crystal.model.DataSourceTest.testSetField", "crystal.model.DataSourceTest.data");

		depsData = runTestAndGetDeps(DataSourceTest.class, "testSetEnabled");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__hide");

		depsData = runTestAndGetDeps(DataSourceTest.class, "testIsHidden");
		has(depsData, "crystal.model.DataSourceTest.testSetField", "crystal.model.DataSourceTest.data");
		// crystal.model.DataSourceTest.testIsHidden,crystal.model.DataSourceTest.testSetEnabled
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__enabled");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetEnabled", "__enabled");

		depsData = runTestAndGetDeps(DataSourceTest.class, "testSetParent");
		depsData = runTestAndGetDeps(DataSourceTest.class, "testSetKind");
		depsData = runTestAndGetDeps(DataSourceTest.class, "testSetCloneString");
		depsData = runTestAndGetDeps(DataSourceTest.class, "testToString");

		has(depsData, "crystal.model.DataSourceTest.testSetCloneString", "__cloneString");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__cloneString");
	}

}