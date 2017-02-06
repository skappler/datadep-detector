package edu.gmu.swe.datadep.edu.gmu.swe.datadep.xstream;

import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import crystal.model.DataSourceTest;
import edu.gmu.swe.datadep.HeapWalker;

public class MarshalTest {

	@Test
	public void marshalDataSource() throws IOException {
		DataSourceTest t = new DataSourceTest();
		t.testSetField();
		System.out.println("MarshalTest.marshalDataSource() t.data " + t.data);
		Element xml = HeapWalker.serialize(t.data);
		XMLOutputter o = new XMLOutputter(Format.getPrettyFormat());
		o.output(xml, System.out);
	}
}
