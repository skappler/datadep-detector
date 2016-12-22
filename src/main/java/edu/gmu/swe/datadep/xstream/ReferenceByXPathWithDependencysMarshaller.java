package edu.gmu.swe.datadep.xstream;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.ReferenceByXPathMarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import edu.gmu.swe.datadep.DependencyInfo;
import edu.gmu.swe.datadep.HeapWalker;
import edu.gmu.swe.datadep.JDomHackWriter;
import edu.gmu.swe.datadep.TagHelper;
import edu.gmu.swe.datadep.struct.WrappedPrimitive;

public class ReferenceByXPathWithDependencysMarshaller extends ReferenceByXPathMarshaller {
	public ReferenceByXPathWithDependencysMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper, int mode) {
		super(writer, converterLookup, mapper, mode);
	}

	@Override
	public void convert(Object item, final Converter converter) {
		super.convert(item, new Converter() {
			@Override
			public boolean canConvert(Class type) {
				return converter.canConvert(type);
			}

			@Override
			public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {

				if (source != null) {
					if (HeapWalker.CAPTURE_TAINTS) {

					} else {
						DependencyInfo inf = TagHelper.getOrFetchTag(source);
						if (source instanceof WrappedPrimitive)
							inf = ((WrappedPrimitive) source).inf;
						if (inf != null && inf.isConflict()) {
							writer.addAttribute("dependsOn", HeapWalker.testNumToTestClass.get(inf.getWriteGen()) + "." + HeapWalker.testNumToMethod.get(inf.getWriteGen()));
						}
						if (source instanceof Map) {
							try {
								Map m = (Map) source;
//								Field f = source.getClass().getDeclaredField("size");
								Field finf;
								if(source.getClass().equals(Hashtable.class)){
									finf = source.getClass().getDeclaredField("count__DEPENDENCY_INFO");
								}else{
									finf = source.getClass().getDeclaredField("size__DEPENDENCY_INFO");
								}
//								f.setAccessible(true);
								finf.setAccessible(true);
								writer.addAttribute("size", "" + m.size());
								inf = (DependencyInfo) finf.get(source);
								if (inf != null && inf.isConflict()) {
									writer.addAttribute("size_dependsOn", HeapWalker.testNumToTestClass.get(inf.getWriteGen()) + "." + HeapWalker.testNumToMethod.get(inf.getWriteGen()));
								}
							} catch (NoSuchFieldException e) {
								System.err.println("Source "+source.getClass());
								e.printStackTrace();
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}
				if(source instanceof String){
					source = ((String)source).trim();
				}
				if(source instanceof char[]){
					source = new String((char[]) source).trim().toCharArray();
				}
				converter.marshal(source, writer, context);
				if (source != null) {
					JDomHackWriter wr = (JDomHackWriter) writer.underlyingWriter();
					DependencyInfo inf = TagHelper.getOrFetchTag(source);
					if(inf != null)
						inf.xmlEl = wr.recentNode;
				}

			}

			@Override
			public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
				return converter.unmarshal(reader, context);
			}
		});
	}
}