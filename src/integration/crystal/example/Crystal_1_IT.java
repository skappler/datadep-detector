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
public class Crystal_1_IT extends AbstractCrystalIT {

	@Test
	public void testEmptySetKind() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		executeTest(DataSourceTest.class, "testSetField");

		// This writes to several part
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);

		Assert.assertTrue(depsData.size() == 0);

		executeTest(DataSourceTest.class, "testSetKind");
		// READ REPO_KIND
		// WRITE REPO_KIND

		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetKind2");
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);

		// Finds this
		has(depsData, "crystal.model.DataSourceTest.testSetField", "crystal.model.DataSourceTest.data");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "__cloneString");
		// This is always missing
		has(depsData, "crystal.model.DataSourceTest.testSetField", "__repoKind");
	}

}