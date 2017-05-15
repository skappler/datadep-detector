package example;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.junit.Assert;

import crystal.model.DataSourceTest;
import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.StaticFieldDependency;

/**
 * @author gambi
 *
 */
public class Crystal_1_IT extends AbstractCrystalIT {

	//	@Test
	public void testEmptySetKind() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		//
		// TODO If something is declared by not initialized while something else
		// is initialize at null which one creates a conflict?
		//
		executeTest(DataSourceTest.class, "testSetField");

		// This writes to several part
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetField");
		depsData = extractAllDepValues(deps);
		Assert.assertTrue(depsData.isEmpty());

		//
		executeTest(DataSourceTest.class, "testSetRemoteCmd");
		/*
		 * assertNull("Get default remote hg", data.getRemoteCmd()); -> access
		 * data --> Conflict -> access data._remoteCmd which is null -> No
		 * Conflict
		 * 
		 * data.setRemoteCmd("remoteHg"); -> access data --> Already Conflict ->
		 * write data._remoteCmd -> No Conflict, last write on this == 2
		 * 
		 * assertTrue("Set remote hg", data.getRemoteCmd().equals("remoteHg"));
		 * -> access data --> Already Conflict -> access data._remoteCmd -> No
		 * conflict
		 * 
		 * TODO Checks: on last write / last read of those fields, on deps that
		 * must not be there
		 * 
		 */
		//
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetRemoteCmd");
		depsData = extractAllDepValues(deps);

		has(depsData, "crystal.model.DataSourceTest.testSetField", "crystal.model.DataSourceTest.data");

		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "crystal.model.DataSource");

		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__remoteCmd");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__testCommand");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__a_second_testCommand");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__a_third_testCommand");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__a_fourth_testCommand ");
		//
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__a_fifth_testCommand");

		/**
		 * Being the last managed by the instrumenter code, magically, this gets
		 * a write somehow during the first test.
		 */
		// System.out.println("Crystal_1_IT.testSetRemoteCmd() " + depsData);
		// //
		// //
		// //
		executeTest(DataSourceTest.class, "testToString");
		/*
		 *
		 * data.setShortName(short_name); -> access data -> conflict -> write
		 * data._shortName -> conflict, last write = 3 data.setKind(kind); ->
		 * access data ---> Already Conflict -> write data._repoKind ->
		 * conflict, last write = 3 data.setCloneString(cloneString); -> access
		 * data ---> Already Conflict -> write data._cloneString-> conflict,
		 * last write = 3 assertTrue("String representation",
		 * data.toString().equals(short_name + "_" + kind + "_" + cloneString));
		 * -> access data ---> Already Conflict -> access data._shortName ->
		 * Already Conflict -> access data._repoKind -> Already Conflict ->
		 * access data._cloneString -> Already Conflict
		 * 
		 * TODO Check: last writes, no additional deps, e.g., data._remoteCmd,
		 * data._testCommand, data._another_testCommand
		 */
		//
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testToString");
		depsData = extractAllDepValues(deps);
		// System.out.println("Crystal_1_IT.testToString() " + depsData);
		has(depsData, "crystal.model.DataSourceTest.testSetField", "crystal.model.DataSourceTest.data");
		has(depsData, "crystal.model.DataSourceTest.testSetField", "__shortName");

		// THIS IS MISSING WHY ?!
		has(depsData, "crystal.model.DataSourceTest.testSetField", "__repoKind");

		has(depsData, "crystal.model.DataSourceTest.testSetField", "__cloneString");
		// -- TODO Not sure why field names have this form _+<FIELD_NAME>
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "crystal.model.DataSource");
		//
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__remoteCmd");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__testCommand");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__a_second_testCommand");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__a_third_testCommand");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__a_fourth_testCommand ");
		hasNot(depsData, "crystal.model.DataSourceTest.testSetField", "__a_fifth_testCommand");
		//
		// System.out.println("Crystal_1_IT.testEmptySetKind() " + deps);
		// // Finds this
		// // has(depsData, "crystal.model.DataSourceTest.testSetField",
		// // "crystal.model.DataSourceTest.data");
		// // has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// // "__cloneString");
		// // This is always missing
		// // has(depsData, "crystal.model.DataSourceTest.testSetField",
		// // "__repoKind");
	}

}