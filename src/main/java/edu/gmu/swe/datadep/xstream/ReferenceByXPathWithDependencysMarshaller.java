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

/*
 * 
 * Still not sure how/when this is invoked ..
 *
 */
public class ReferenceByXPathWithDependencysMarshaller extends ReferenceByXPathMarshaller {
	public ReferenceByXPathWithDependencysMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup,
			Mapper mapper, int mode) {
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
						// This will return a dep object even if the object is
						// null, but every time an object is null it generates a
						// new one !
						DependencyInfo inf = TagHelper.getOrFetchTag(source);
						// if source is a string it might be null we need to
						// check that
						if (source instanceof WrappedPrimitive) {
							inf = ((WrappedPrimitive) source).inf;
						}
						// else if (source instanceof String) {
						// inf = ((WrappedPrimitive) source).inf; /* Ok ?! */
						// }

						if (inf != null && inf.isConflict()) {
							if (HeapWalker.testNumToTestClass.get(inf.getWriteGen()) == null) {
								System.out.println("FOUND NULL RBXPath " + inf.getWriteGen() + " "
										+ HeapWalker.testNumToTestClass.size());
							} else {
								writer.addAttribute("dependsOn", HeapWalker.testNumToTestClass.get(inf.getWriteGen())
										+ "." + HeapWalker.testNumToMethod.get(inf.getWriteGen()));

								writer.addAttribute("setBy", "ReferenceByXPathWithDependencysMarshaller");
							}
						}
						if (source instanceof Map) {
							try {
								Map m = (Map) source;
								// Field f =
								// source.getClass().getDeclaredField("size");
								Field finf;
								if (source.getClass().equals(Hashtable.class)) {
									finf = source.getClass().getDeclaredField("count__DEPENDENCY_INFO");
								} else {
									try {
										finf = source.getClass().getDeclaredField("size__DEPENDENCY_INFO");
									} catch (NoSuchFieldException e) {
										// System.out.println("NoSuchFieldException
										// for map type " + source.getClass());
										finf = null;
									}
								}
								// f.setAccessible(true);

								if (finf != null) {
									finf.setAccessible(true);
									writer.addAttribute("size", "" + m.size());
									inf = (DependencyInfo) finf.get(source);
									if (inf != null && inf.isConflict()) {
										if (HeapWalker.testNumToTestClass.get(inf.getWriteGen()) == null) {
											System.out.println("FOUND NULL SDO " + inf.getWriteGen() + " "
													+ HeapWalker.testNumToTestClass.size());
										} else {
											writer.addAttribute("size_dependsOn",
													HeapWalker.testNumToTestClass.get(inf.getWriteGen()) + "."
															+ HeapWalker.testNumToMethod.get(inf.getWriteGen()));
											writer.addAttribute("setBy", "ReferenceByXPathWithDependencysMarshaller");
										}
									}
								}
							} catch (NoSuchFieldException e) {
								System.err.println("Source " + source.getClass());
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
				} else {
					System.out.println(
							"ReferenceByXPathWithDependencysMarshaller.convert(...).new Converter() {...}.marshal() source is null");
				}

				// TODO Not sure what is doing here ...
				// Why this does not fail for String == null ?
				if (source instanceof String) {
					source = ((String) source).trim();
				}
				if (source instanceof char[]) {
					source = new String((char[]) source).trim().toCharArray();
				}

				// TODO What's this ?
				converter.marshal(source, writer, context);

				if (source != null) {
					JDomHackWriter wr = (JDomHackWriter) writer.underlyingWriter();
					DependencyInfo inf = TagHelper.getOrFetchTag(source);
					if (inf != null) {
						inf.xmlEl = wr.recentNode;
					}
				}

			}

			@Override
			public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
				return converter.unmarshal(reader, context);
			}
		});
	}
}