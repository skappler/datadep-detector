package example;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import edu.gmu.swe.datadep.DependencyInfo;
import edu.gmu.swe.datadep.HeapWalker;

public class HeapWalkerTest {

	@After
	public void clearWaw() {
		System.getProperties().remove(HeapWalker.USE_WAW);
	}

	@Test
	public void testWawOn() {
		System.setProperty(HeapWalker.USE_WAW, "true");
		try {
			HeapWalker.walkAndFindDependencies("test", "test");
		} catch (Exception e) {

		}
		org.junit.Assert.assertTrue(DependencyInfo.conflictsForWriteAfterWrite);
	}

	@Test
	public void testWawOff() {
		System.setProperty(HeapWalker.USE_WAW, "false");
		try {
			HeapWalker.walkAndFindDependencies("test", "test");
		} catch (Exception e) {

		}
		org.junit.Assert.assertFalse(DependencyInfo.conflictsForWriteAfterWrite);

	}

	@Test
	public void testDefaultWaw() {
		Assert.assertNull(System.getProperty(HeapWalker.USE_WAW));
		try {
			HeapWalker.walkAndFindDependencies("test", "test");
		} catch (Exception e) {

		}
		org.junit.Assert.assertFalse(DependencyInfo.conflictsForWriteAfterWrite);

	}
}
