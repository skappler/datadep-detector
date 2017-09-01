package utils;

import org.junit.Assert;
import org.junit.Test;

import edu.gmu.swe.datadep.Enumerations;

public class IgnoreTest {

	@Test
	public void testIgnoreGeneratedEnumerations() {
		Enumerations.get().add("org.jsoup.parser.TokeniserState");
		String owner = "org/jsoup/parser/TokeniserState$1";

		Assert.assertTrue(Enumerations.get().contains(owner.replaceAll("/", ".").replaceAll("\\$[0-9][0-9]*", "")));

		owner = "org/jsoup/parser/TokeniserState$2";

		Assert.assertTrue(Enumerations.get().contains(owner.replaceAll("/", ".").replaceAll("\\$[0-9][0-9]*", "")));

				owner = "org/jsoup/parser/TokeniserState";

		Assert.assertTrue(Enumerations.get().contains(owner.replaceAll("/", ".").replaceAll("\\$.*", "")));

	}
}
