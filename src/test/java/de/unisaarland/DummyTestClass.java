package de.unisaarland;

import org.junit.Test;

//@Ignore // Will this apply to JUnit core as well ?! Yes, it does ...
public class DummyTestClass {

	@Test
	public void dummyTest() {
		System.out.println("DummyTestClass.dummyTest()");
	}

	@Test
	public void anotherDummyTest() {
		System.out.println("DummyTestClass.anotherDummyTest()");
	}
}
