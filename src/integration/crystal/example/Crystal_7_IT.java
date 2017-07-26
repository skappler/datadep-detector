package example;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import crystal.model.DataSourceTest;
import edu.gmu.swe.datadep.DependencyInfo;
import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.StaticFieldDependency;
import edu.gmu.swe.datadep.inst.DependencyTrackingClassVisitor;

/**
 * @author gambi
 *
 */
public class Crystal_7_IT extends AbstractCrystalIT {

	@BeforeClass
	public static void debug() {
//		DependencyTrackingClassVisitor.fieldsLogged.add(Pattern.compile("crystal.model.DataSource"));
		// Not sure why this does not work... Maybe those fields are simply not
		// processed unless they are static
		// DependencyTrackingClassVisitor.fieldsLogged.add(Pattern.compile("crystal.*_repoKind"));
		//		DependencyTrackingClassVisitor.fieldsLogged.add(Pattern.compile(".*"));
	}

	/**
	 * In some cases a test only writes, while another reads and write. This
	 * shall be marked as dependency as well as once the order of execution of
	 * those test is reverted, manifest dependencies might cause tests to fail
	 */
	@Test
	public void testWriteAfterWrite() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		/*
		 * Write SF data Write data._short_name Write data._clone_string Write
		 * data._repo_kind Write data._hide, Write data._parent Write
		 * data._enabled
		 * 
		 * Write to null data._history; Write to null data._old_history;
		 */
		executeTest(DataSourceTest.class, "testSetField");
		// First test no conflicts nor dependencies
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetField");
		// At this point we are still allowing for dependencies to be collected
		// - For example during debug if I hover over the class it will actually
		// trigger the reads
		// !
		DependencyInfo.IN_CAPTURE = true;
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		// Access SF data -> conflict 1
		// Access data._cloneString -> conflict 1
		// Write data._cloneString -> Already in conflict
		// Access data._cloneString -> Already in conflict
		DependencyInfo.IN_CAPTURE = false;
		executeTest(DataSourceTest.class, "testSetCloneString");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetCloneString");
		DependencyInfo.IN_CAPTURE = true;
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);

		has(depsData, "crystal.model.DataSourceTest.testSetField", "crystal.model.DataSourceTest.data");
		has(depsData, "crystal.model.DataSourceTest.testSetField", "__cloneString");
		//
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__enabled");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__hide");

		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__shortName");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__parent");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__repoKind");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__remoteCmd");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__compileCommand");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__testCommand");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__history");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__oldHistory");
		DependencyInfo.IN_CAPTURE = false;
		// Access data -> conflict 1
		// Write _shortName -> conflict 1
		// Write _repoKind -> conflict 1
		// Write _cloneString -> conflict 2
		// Access shortName -> already conflict
		// Access _repoKind -> already conflict
		// Access _ cloneString -> Already conflict
		executeTest(DataSourceTest.class, "testToString");
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testToString");
		DependencyInfo.IN_CAPTURE = true;
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);

		///
		has(depsData, "crystal.model.DataSourceTest.testSetField", "crystal.model.DataSourceTest.data");
		//
		has(depsData, "crystal.model.DataSourceTest.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTest.testSetField", "__repoKind");
		//
		has(depsData, "crystal.model.DataSourceTest.testSetCloneString", "__cloneString");
		//
		hasNot(depsData, Pattern.compile(".*"), "__enabled");
		hasNot(depsData, Pattern.compile(".*"), "__hide");
		hasNot(depsData, Pattern.compile(".*"), "__parent");
		hasNot(depsData, Pattern.compile(".*"), "__remoteCmd");
		hasNot(depsData, Pattern.compile(".*"), "__compileCommand");
		hasNot(depsData, Pattern.compile(".*"), "__testCommand");
		hasNot(depsData, Pattern.compile(".*"), "__history");
		hasNot(depsData, Pattern.compile(".*"), "__oldHistory");
		DependencyInfo.IN_CAPTURE = false;
	}

}