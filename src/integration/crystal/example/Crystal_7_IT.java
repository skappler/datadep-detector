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

		// HeapWalker.walkAndFindDependencies("INIT", "INIT");

		(new DataSourceTest()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		// System.out.println("CrystalIT.testWriteAfterWrite() " + deps);

		// This one read and set some variables
		(new DataSourceTest()).testSetCloneString();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetCloneString");
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTest.testSetField", "__cloneString");

		// This one just set some variables. However, by doing so, it introduce
		// an error if this is used before
		// testSetCloneString which expects a different string. So the dep among
		// the two is a manifest dep. How to check this ?! Read gen ?
		(new DataSourceTest()).testToString();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testToString");

		// System.out.println("CrystalIT.testWriteAfterWrite() " + deps);

		// Writes after writes
		// data.setShortName(short_name); WW
		// data.setKind(kind); WW
		// data.setCloneString(cloneString); WW

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