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
public class Crystal_7_IT extends AbstractCrystalIT {

	/**
	 * In some cases a test only writes, while another reads and write. This
	 * shall be marked as dependency as well as once the order of execution of
	 * those test is reverted, manifest dependencies might cause tests to fail
	 */
	@Test
	public void testWriteAfterWrite() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		// NOTE that this uses DataSourceTest not DataSourceTestAlessio ...
		executeTest(DataSourceTest.class, "testSetField");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		// This one read and set some variables
		executeTest(DataSourceTest.class, "testSetCloneString");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetCloneString");
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTest.testSetField", "__cloneString");

		executeTest(DataSourceTest.class, "testToString");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testToString");
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);
		// TODO For the moment, Write after Write are not consider data

		// dependency
		// has(depsData, "crystal.model.DataSourceTest.testSetCloneString",
		// "__cloneString");
		// has(depsData, "crystal.model.DataSourceTest.testSetField",
		// "__shortName");
		//
		// has(depsData, "crystal.model.DataSourceTest.testSetField",
		// "__repoKind");
	}

}