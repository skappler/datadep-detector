package edu.gmu.swe.datadep.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import edu.gmu.swe.datadep.DependencyInfo;
import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.struct.WrappedPrimitive;

public class WrappedPrimitiveConverter implements Converter {
	@Override
	public boolean canConvert(Class type) {
		return type == WrappedPrimitive.class;
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		return null;
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {

		// Does this triggers a read ?
		System.out.println("WrappedPrimitiveConverter.marshal() 1 ");
		DependencyInfo dep = ((WrappedPrimitive) source).inf;
		System.out.println("WrappedPrimitiveConverter.marshal() 2 ");
		
		
		if (dep != null && dep.isConflict()) {
			if (HeapWalker.testNumToTestClass.get(dep.getWriteGen()) == null) {
				System.out.println("FOUND NULL WP " + dep.getWriteGen() + " " + HeapWalker.testNumToTestClass.size()
						+ " at Object " + ((WrappedPrimitive) source).prim.getClass());
			} else {
				System.out.println("WrappedPrimitiveConverter.marshal() Found a conflict for ");
				//
				writer.addAttribute("dependsOn", HeapWalker.testNumToTestClass.get(dep.getWriteGen()) + "."
						+ HeapWalker.testNumToMethod.get(dep.getWriteGen()));
				writer.addAttribute("dependsOnId", "" + dep.getWriteGen());
				writer.addAttribute("setBy", "WrappedPrimitiveConverter");
			}
		}

		// Now by definition primitives cannot be null, however, we consider
		// String as primitive, and they can be null !
		// if we call toString in that can we generate a NPE
		String val = ((((WrappedPrimitive) source).prim != null) ? ((WrappedPrimitive) source).prim.toString() : null);

		if (val != null && !val.trim().equals("")) {
			writer.setValue(val);
		}
	}
}