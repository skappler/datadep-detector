package example;

import java.io.IOException;
import java.io.StringReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import crystal.model.DataSourceTestAlessio;
import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.StaticFieldDependency;

/**
 * @author gambi
 *
 */
public class CrystalIT {

	private static utils.ClassLoaderHelper ch = new utils.ClassLoaderHelper();

	@BeforeClass
	public static void preloadAllClasses() throws ClassNotFoundException {
		// If we simply do the preloading, we miss the whitelist
		HeapWalker.clearWhitelist();
		HeapWalker.addToWhitelist("crystal");

		ch.getClassesForPackage("crystal");

	}

	@Before
	public void setupWhitelist() {
		HeapWalker.clearWhitelist();
		HeapWalker.addToWhitelist("crystal");
		//
	}

	@After
	public void clearAll() {
		HeapWalker.resetAllState();
	}

	// Copied from java.lang.reflect.Field
	private static String getTypeName(Class<?> type) {
		if (type.isArray()) {
			try {
				Class<?> cl = type;
				int dimensions = 0;
				while (cl.isArray()) {
					dimensions++;
					cl = cl.getComponentType();
				}
				StringBuffer sb = new StringBuffer();
				sb.append(cl.getName());
				for (int i = 0; i < dimensions; i++) {
					sb.append("[]");
				}
				return sb.toString();
			} catch (Throwable e) {
				/* FALLTHRU */ }
		}
		return type.getName();
	}

	// Return the string/xml of the value for this sf
	private Collection<Entry<String, String>> extractDataStaticFieldDepValue(LinkedList<StaticFieldDependency> deps) {
		for (StaticFieldDependency sf : deps) {
			if (sf.field.getType().isAssignableFrom(DataSourceTestAlessio.data.getClass())) {
				return extractDepsData(sf);
			}
		}
		Assert.fail("Cannot find value of data static field as dep");
		return null;
	}

	private Collection<Entry<String, String>> extractDepsData(StaticFieldDependency sf) {
		List<Entry<String, String>> deps = new ArrayList<Entry<String, String>>();
		// Extract root dep
		deps.add(new AbstractMap.SimpleEntry<String, String>(
				(getTypeName(sf.field.getDeclaringClass()) + "." + sf.field.getName()), sf.on));

		// Extract deps on values
		String xmlValue = sf.value;
		SAXBuilder builder = new SAXBuilder();
		try {

			Document document = (Document) builder.build(new StringReader(xmlValue));

			// Select only elements with the dependsOn attribute
			String query = "//*[@dependsOn]";
			XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());
			for (Element urle : xpe.evaluate(document)) {
				Entry<String, String> dep = new AbstractMap.SimpleEntry<String, String>(urle.getName(),
						urle.getAttribute("dependsOn").getValue());
				deps.add(dep);
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
			Assert.fail(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
			Assert.fail(jdomex.getMessage());
		}

		return deps;
	}

	@Test
	public void testNoEmptyReadAfterSetField() {
		// force and ignore static initializers
		System.out.println(HeapWalker.walkAndFindDependencies("INIT", "INIT"));

		LinkedList<StaticFieldDependency> deps;
		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");

		(new DataSourceTestAlessio()).testReadALL();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL");

		Collection<Entry<String, String>> depsData = extractDataStaticFieldDepValue(deps);

		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "a");
	}

	private static void has(Collection<Entry<String, String>> depsData, String testName, String depName) {
		for (Entry<String, String> d : depsData) {
			if (d.getValue().equals(testName) && d.getKey().equals(depName)) {
				return;
			}
		}
		// TODO Potential Alternatives - help debugging
		Assert.fail("Missing dep: " + depName + " -- " + testName + " in " + depsData);
	}

	@Test
	public void testEmptySetKind() {
		// force and ignore static initializers
		System.out.println(HeapWalker.walkAndFindDependencies("INIT", "INIT"));

		LinkedList<StaticFieldDependency> deps;
		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");

		(new DataSourceTestAlessio()).testSetKind();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");

		// Assertions
		Collection<Entry<String, String>> depsData = extractDataStaticFieldDepValue(deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
	}

	@Test
	public void testEmptySetKindAfterRead() {

		System.out.println(HeapWalker.walkAndFindDependencies("INIT", "INIT"));
		LinkedList<StaticFieldDependency> deps;

		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");
		System.out.println("setField" + deps);

		(new DataSourceTestAlessio()).testReadALL();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL");
		System.out.println("testReadALL" + deps);

		Collection<Entry<String, String>> depsData = extractDataStaticFieldDepValue(deps);

		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "a");

		(new DataSourceTestAlessio()).testSetKind();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");
		depsData = extractDataStaticFieldDepValue(deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");

		// Assertions
		extractDataStaticFieldDepValue(deps);
	}

	@Test
	public void testEmptySetKindAfterClone() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		System.out.println(HeapWalker.walkAndFindDependencies("INIT", "INIT"));

		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");

		(new DataSourceTestAlessio()).testSetCloneString();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCloneString");
		depsData = extractDataStaticFieldDepValue(deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");

		(new DataSourceTestAlessio()).testSetKind();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");
		depsData = extractDataStaticFieldDepValue(deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data"); // TODO
																														// Not
																														// sure
																														// !
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetCloneString", "__cloneString");
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
	}

	@Test
	public void testMultipleReads() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		// force and ignore static initializers
		System.out.println(HeapWalker.walkAndFindDependencies("INIT", "INIT"));

		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");

		(new DataSourceTestAlessio()).testReadALL();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL");
		//
		depsData = extractDataStaticFieldDepValue(deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "a");

		(new DataSourceTestAlessio()).testReadALL2();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL2");
		//
		depsData = extractDataStaticFieldDepValue(deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "a");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");

		(new DataSourceTestAlessio()).testReadALL3();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL3");
		//
		depsData = extractDataStaticFieldDepValue(deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "a");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__hide");

		(new DataSourceTestAlessio()).testReadALL4();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL4");
		//
		depsData = extractDataStaticFieldDepValue(deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "a");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__hide");

		(new DataSourceTestAlessio()).testSetCloneString();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCloneString");
		//
		depsData = extractDataStaticFieldDepValue(deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");

		(new DataSourceTestAlessio()).testSetKind();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");
		System.out.println("testSetKind" + deps);
		// dependsOn="crystal.model.DataSourceTestAlessio.testSetCloneString" on
		// __cloneString
		// dependsOn="crystal.model.DataSourceTestAlessio.testSetField" on
		// __kind,

		//
		depsData = extractDataStaticFieldDepValue(deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data"); // NOT
																														// SURE
																														// ABOUT
																														// THIS
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetCloneString", "__cloneString");
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
	}

}