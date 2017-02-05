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
public class Crystal_5_IT extends AbstractCrystalIT {

	@Test
	public void testNoEmptyReadAfterSetField() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;
		// force and ignore static initializers
		// HeapWalker.walkAndFindDependencies("INIT", "INIT");

		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		(new DataSourceTestAlessio()).testReadALL();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL");

		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);

		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// Removed attribute a
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");
		//
		// Reads on null value shall be reported ? Ideally no, because for
		// objects null values are default.
		// But what if someone actually wrote a null value into a field ?!
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "__compileCommand");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "__reniteCmd");

	}

}