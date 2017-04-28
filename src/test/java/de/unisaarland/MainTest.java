package de.unisaarland;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class MainTest {

	// TODO Check if the file exists before the test
	@Test
	public void emptyTestList() throws FileNotFoundException, ClassNotFoundException, IOException {
		DependencyCollector m = new DependencyCollector();
		m.main(new String[] { "--output-file", "src/test/resources/empty-deps.csv",
				"src/test/resources/empty-test-list.txt" });
	}

	@Test
	public void basicTestList() throws FileNotFoundException, ClassNotFoundException, IOException {
		DependencyCollector m = new DependencyCollector();
		m.main(new String[] { "--output-file", "src/test/resources/deps.csv", "src/test/resources/test-list.txt" });

	}

	@Test
	public void parseTestList() throws FileNotFoundException, ClassNotFoundException, IOException {
		DependencyCollector m = new DependencyCollector();
		m.main(new String[] { "--tests", "de.unisaarland.DummyTestClass.dummyTest,de.unisaarland.DummyTestClass.anotherDummyTest" });
	}
}
