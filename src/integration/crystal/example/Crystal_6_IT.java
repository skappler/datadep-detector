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
public class Crystal_6_IT extends AbstractCrystalIT {

	@Test
	public void testNoEmptyReadAfterSetNullField() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		// force and ignore static initializers
		// HeapWalker.walkAndFindDependencies("INIT", "INIT");

		// initialize the data structure
		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");

		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		// Set the field to a non null value
		(new DataSourceTestAlessio()).testSetCompileCommand();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCompileCommand");

		// Changes as well, so the conflict must be updated in the next
		// execution
		(new DataSourceTestAlessio()).testSetCompileCommandToNull();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCompileCommandToNull");

		// Assert
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		// We read data so we need it - data is a SF
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		// The field _compileCommand was changed to null ... this must be
		// reported !
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetCompileCommand", "__compileCommand");

		// Set the field to a non null value
		(new DataSourceTestAlessio()).testSetCompileCommand();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCompileCommand");
		// Assert
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		// We read data so we need it - data is a SF
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		// The field _compileCommand was changed again
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetCompileCommandToNull", "__compileCommand");

	}

}