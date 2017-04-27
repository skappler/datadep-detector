package org.dreads;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

public class Properties {
	private static final java.util.Properties properties = new java.util.Properties();
	static {
		/**
		 * Load properties
		 */
		try {
			final FileInputStream input;
			if (System.getenv("DYNAFLOW_HOME") != null)
				input = new FileInputStream(System.getenv("DYNAFLOW_HOME") + "//dreads.properties");
			else
				input = new FileInputStream("dreads.properties");
			properties.load(input);
			input.close();
		} catch (IOException e) {
			System.err.println("Missing property file. Please set DYNAFLOW_HOME environment variable");
		}
	}

	    /** for debug purpose, print in dot and png the reference graph after the execution of each test case */
		public static final boolean PRINTREFGRAPH = Boolean.parseBoolean(properties.getProperty("print_graph", "true"));
		/**for debug/small examples. Print A LOT of stuff in the console */
		public static final boolean SYSOPRINT = Boolean.parseBoolean(properties.getProperty("print_out", "false")); 
		/**enable uses recognitions as well */
		public static final boolean USES = Boolean.parseBoolean(properties.getProperty("track_uses", "true"));
		/**handle test cases that raise exceptions, but do not support yet nested calls between test cases (e.g. test1 calls test0) */
		public static final boolean STRONGTESTCLEAN = Boolean.parseBoolean(properties.getProperty("strong_clean", "true"));
		/**serialize results collapsing indexes of arrays and lists"**/
		public static final boolean SERIALIZE = Boolean.parseBoolean(properties.getProperty("serialize", "false"));
		/**serialize results collapsing indexes of arrays and lists"**/
		public static final boolean SERIALIZENOINDEX = Boolean.parseBoolean(properties.getProperty("serializy_no_index", "true"));
		/** bind the data flow events to the method chain of invocations. These values have to be true when using the analysis 
		 * for identifying test objectives, false when using the analysis to capture precise data flow events 
		 * (e.g. mod analysis or slicing) **/
		public static final boolean FILTER_USES = USES && Boolean.parseBoolean(properties.getProperty("data_flow_testing", "true"));
		public static final boolean FILTER_DEFS = Boolean.parseBoolean(properties.getProperty("data_flow_testing", "true"));
		
		/** timeout in millisecs, after timeout the analysis is disabled until the execution of the next test case **/
		public static final long TIMEOUT = Long.parseLong(properties.getProperty("timeout", "200000"));
		
		/** deprecated */
		public static final boolean IMPLICIT_USES = USES && false;
		
		public static boolean DEBUG = false;
		
		/**
		 * scope of packages that will be analyzed - the DiSL code for parsing the
		 * regex is buggy, if you need some more specific constraint you should look
		 * into the Scope class in DiSL and change it
		 */
		
		public static final String FOLDERNAME = "results";

		public static final String SCOPE = "coffeemaker.*.*(*)";

		
		/** name that must be contained in method name and class of test cases to be recognised as tests */
		public static final String TEST_METHODS=properties.getProperty("test_method_names", "test");
		public static final String TESTCLASS=properties.getProperty("test_class_names", "test");
		
		/** print files with information */
		public static final boolean PRINTDF = Boolean.parseBoolean(properties.getProperty("print_output", "true"));
		
		/**
		 * true: you can miss some defs in some special case, false, you can report
		 * some defs on objects that were garbage collected - true is better imo.
		 */
		public static boolean CLEAN_GRAPH = Boolean.parseBoolean(properties.getProperty("clean_graph", "true"));
		
		/** dot path if you want to print dot graphs */
		public static String DOT_PATH = "/opt/local/bin/dot";
		
		/**
		 * if for some reason you have to filer out some class, put it here
		 */
		public static final List<String> filteredClasses = Lists.newArrayList("com/lowagie/text/pdf", "org/evosuite", "dk/brics", "FibonacciHeap");//"org/jfree/chart/renderer/AbstractRenderer", "org/jfree/chart/plot/CategoryPlot", "org/jfree/chart/plot/XYPlot"
			
		/**
		 * my stuff
		 */
		public static boolean LONGNAMES = true;
		
		/** deprecated, false */
		public static final boolean TRACE = false;  
}

//public static final String SCOPE = "visu.handball.*.*(*)";
//public static final String SCOPE = "defanduses.*.*(*)";
//public static final String SCOPE = "dread.*.*(*)";  
//public static final String SCOPE = "org.jfree.*.*(*)";  
//public static final String SCOPE = "org.graphstream.*.*(*)";
//public static final String SCOPE = "org.jgrazt.*.*(*)";
//public static final String SCOPE = "de.susebox.*.*(*)";  
//public static final String SCOPE = "org.apache.commons.lang3.*.*(*)";
//public static final String SCOPE = "org.apache.commons.collections.*.*(*)";