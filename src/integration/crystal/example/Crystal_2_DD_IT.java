package example;

import java.util.Set;

import org.junit.Test;

import crystal.model.DataSourceTestAlessio;
import edu.gmu.swe.datadep.DataDependency;
import edu.gmu.swe.datadep.HeapWalker;

/**
 * @author gambi
 *
 */
public class Crystal_2_DD_IT extends AbstractCrystalIT {

	@Test
	public void testEmptySetKinDataDependency() {
		Set<DataDependency> deps;

		executeTest(DataSourceTestAlessio.class, "testSetField");

		deps = HeapWalker.findDependencies("crystal.model.DataSourceTestAlessio", "testSetField");
		System.out.println("Crystal_2_IT_DD.testEmptySetKindAfterClone() Found " + deps.size() + " deps ");

		executeTest(DataSourceTestAlessio.class, "testSetCloneString");
		deps = HeapWalker.findDependencies("crystal.model.DataSourceTestAlessio", "testSetCloneString");
		System.out.println("Crystal_2_IT_DD.testEmptySetKindAfterClone() Found " + deps.size() + " deps ");

		executeTest(DataSourceTestAlessio.class, "testSetKind");
		deps = HeapWalker.findDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");
		System.out.println("Crystal_2_IT_DD.testEmptySetKindAfterClone() Found " + deps.size() + " deps ");
	}

}