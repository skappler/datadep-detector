package example;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import crystal.model.DataSourceTestAlessio;
import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.StaticFieldDependency;

/**
 * @author gambi
 *
 */
public class Crystal_2_IT extends AbstractCrystalIT {

	@Test
	public void testEmptySetKindAfterClone() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		executeTest(DataSourceTestAlessio.class, "testSetField");

		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		executeTest(DataSourceTestAlessio.class, "testSetCloneString");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCloneString");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);

		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");

		executeTest(DataSourceTestAlessio.class, "testSetKind");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetCloneString", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
	}

}