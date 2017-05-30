package example.data;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class DummyTest {

	// Write Map -> no conflict, Write Gen = 0
	private static Map static_map;
	private static Integer i;

	// Map instance_map;
	// String instance_string;

	// @Before
	// public void setup() {
	// System.out.println("DummyTest.testSet()");
	// instance_map = new HashMap<>();
	// instance_map.put("B", "B");
	// instance_string = "A String";
	// }

	@Test
	public void testSet() {
		// System.out.println("DummyTest.testSet()");
		i = 10;
		// Write i -> no conflict, W=1
		static_map = new HashMap<>();
		// Write Map -> no conflict, W=1
		static_map.put("A", "A");
		// Access Map -> no conflict
		// "Write" Map -> W=1
		static_map.put("C", "A");
		// Access Map -> no conflict
		// "Write" Map -> W=1
	}
	// Access Map -> no conflict
	// Write Map -> W=1

	@Test
	public void testGet() {
		// System.out.println("DummyTest.testGet()");
		Assert.assertEquals(10, i.intValue());
		// Access Map -> Conflict
		static_map.get("A");
	}

}
