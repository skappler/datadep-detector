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

import crystal.model.DataSourceTest;
import crystal.model.DataSourceTestAlessio;
import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.StaticFieldDependency;

/**
 * @author gambi
 *
 */
public class CrystalIT {

	@Before
	public void setupWhitelist() {
		HeapWalker.resetAllState();
		//
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
	private Collection<Entry<String, String>> extractDataStaticFieldDepValue(Class dataSourceTestClass,
			LinkedList<StaticFieldDependency> deps) {

		for (StaticFieldDependency sf : deps) {
			if (sf.field.getType().isAssignableFrom(dataSourceTestClass)) {
				return extractDepsData(sf);
			}
		}
		// Assert.fail("Cannot find value of data static field as dep");
		return new ArrayList<Entry<String, String>>();
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
	public void testNoEmptyReadAfterSetNullField() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		// force and ignore static initializers
		// HeapWalker.walkAndFindDependencies("INIT", "INIT");

		// initialize the data structure
		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");

		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		// Set the field to a non null value
		(new DataSourceTestAlessio()).testSetCompileCommand();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCompileCommand");

		// Changes as well, so the conflict must be updated in the next
		// execution
		(new DataSourceTestAlessio()).testSetCompileCommandToNull();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCompileCommandToNull");

		// Assert
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		// We read data so we need it - data is a SF
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		// The field _compileCommand was changed to null ... this must be
		// reported !
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetCompileCommand", "__compileCommand");

		// Set the field to a non null value
		(new DataSourceTestAlessio()).testSetCompileCommand();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCompileCommand");
		// Assert
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		// We read data so we need it - data is a SF
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		// The field _compileCommand was changed again
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetCompileCommandToNull", "__compileCommand");

	}

	@Test
	public void testNoEmptyReadAfterSetField() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;
		// force and ignore static initializers
		// HeapWalker.walkAndFindDependencies("INIT", "INIT");

		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		(new DataSourceTestAlessio()).testReadALL();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL");

		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);

		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// Removed attribute a
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");
		//
		// Reads on null value shall be reported ? Ideally no, because for
		// objects null values are default.
		// But what if someone actually wrote a null value into a field ?!
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "__compileCommand");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "__reniteCmd");

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
		// HeapWalker.walkAndFindDependencies("INIT", "INIT");

		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;
		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		(new DataSourceTestAlessio()).testSetKind();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");

		// Assertions
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
	}

	@Test
	public void testEmptySetKindAfterRead() {

		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		(new DataSourceTestAlessio()).testReadALL();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL");

		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);

		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");

		(new DataSourceTestAlessio()).testSetKind();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");

		// Assertions
		// depsData =
		// extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(),
		// deps);

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

		// HeapWalker.walkAndFindDependencies("INIT", "INIT");

		(new DataSourceTest()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		// System.out.println("CrystalIT.testWriteAfterWrite() " + deps);

		(new DataSourceTest()).testSetCloneString();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testSetCloneString");
		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTest.testSetField", "__cloneString");

		(new DataSourceTest()).testToString();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTest", "testToString");

		// System.out.println("CrystalIT.testWriteAfterWrite() " + deps);

		// Writes after writes
		// data.setShortName(short_name); WW
		// data.setKind(kind); WW
		// data.setCloneString(cloneString); WW

		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);
		// TODO For the moment, Write after Write are not consider data dependency
		// has(depsData, "crystal.model.DataSourceTest.testSetCloneString",
		// "__cloneString");
		// has(depsData, "crystal.model.DataSourceTest.testSetField",
		// "__shortName");
		//
		// has(depsData, "crystal.model.DataSourceTest.testSetField",
		// "__repoKind");
	}

	@Test
	public void testEmptySetKindAfterClone() {
		LinkedList<StaticFieldDependency> deps;
		Collection<Entry<String, String>> depsData;

		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		(new DataSourceTestAlessio()).testSetCloneString();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCloneString");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");

		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");

		(new DataSourceTestAlessio()).testSetKind();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
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
		// HeapWalker.walkAndFindDependencies("INIT", "INIT");

		(new DataSourceTestAlessio()).testSetField();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetField");
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		depsData = extractDataStaticFieldDepValue(DataSourceTest.data.getClass(), deps);
		Assert.assertTrue(depsData.size() == 0);

		(new DataSourceTestAlessio()).testReadALL();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL");
		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		//
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");

		(new DataSourceTestAlessio()).testReadALL2();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL2");
		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");

		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");

		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");

		(new DataSourceTestAlessio()).testReadALL3();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL3");
		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__hide");

		(new DataSourceTestAlessio()).testReadALL4();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "readALL4");
		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__shortName");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__testCommand");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__parent");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		// has(depsData, "crystal.model.DataSourceTestAlessio.testSetField",
		// "a");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__hide");

		(new DataSourceTestAlessio()).testSetCloneString();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetCloneString");
		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "crystal.model.DataSourceTestAlessio.data");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__repoKind");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__cloneString");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__enabled");
		has(depsData, "crystal.model.DataSourceTestAlessio.testSetField", "__remoteCmd");

		(new DataSourceTestAlessio()).testSetKind();
		deps = HeapWalker.walkAndFindDependencies("crystal.model.DataSourceTestAlessio", "testSetKind");
		// dependsOn="crystal.model.DataSourceTestAlessio.testSetCloneString" on
		// __cloneString
		// dependsOn="crystal.model.DataSourceTestAlessio.testSetField" on
		// __kind,

		//
		depsData = extractDataStaticFieldDepValue(DataSourceTestAlessio.data.getClass(), deps);
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