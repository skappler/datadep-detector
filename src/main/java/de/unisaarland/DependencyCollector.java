
package de.unisaarland;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.lexicalscope.jewel.cli.CliFactory;

import de.unisaarland.analysis.CompactDataDependency;
import de.unisaarland.analysis.DataDepEventHandler;
import de.unisaarland.analysis.DataDependency;

public class DependencyCollector {

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		// System.out.println("DependencyCollector.main()");
		ParsingInterface parsedInputs = CliFactory.parseArgumentsUsingInstance(new ParsingInterface(), args);
		JUnitCore core = new JUnitCore();

		final Map<Description, Failure> failedTests = new HashMap<Description, Failure>();
		int totalTests = 0;

		///
		final List<DataDependency> dataDependencies = new ArrayList<DataDependency>();
		final Set<CompactDataDependency> compactDataDependencies = new LinkedHashSet<CompactDataDependency>();

		// TODO Check PreparingTestListener
		core.addListener(new RunListener() {

			private int depsBefore;

			// TODO Use matchers and patterns
			@Override
			public void testStarted(Description description) throws Exception {
				String[] d = description.toString().replace("(", " ").replace(")", "").split(" ");
				String testClass = d[1];
				String testMethod = d[0];

				System.out.println("EXECUTING: " + testClass + "." + testMethod);
				DataDepEventHandler.instanceOf().beforeTestExecution(testClass, testMethod);

			}

			@Override
			public void testFailure(Failure failure) throws Exception {
				//
				failedTests.put(failure.getDescription(), failure);
			}

			@Override
			public void testFinished(Description description) throws Exception {
				String[] d = description.toString().replace("(", " ").replace(")", "").split(" ");
				String testClass = d[1];
				String testMethod = d[0];
				if (failedTests.containsKey(description)) {
					System.out.println("TEST " + testClass + "." + testMethod + " FAILED");
				} else {
					System.out.println("TEST " + testClass + "." + testMethod + " PASSED");
				}
				DataDepEventHandler.instanceOf().afterTestExecution();

				//
				compactDataDependencies.addAll(DataDepEventHandler.instanceOf().getDataDependencies());
				//
				// Missing Recheability from Starting Static Field !
				System.out.println("Found " + (compactDataDependencies.size() - depsBefore) + " deps for " + testClass
						+ "." + testMethod);
				depsBefore = compactDataDependencies.size();
				//
				if (!parsedInputs.isCompact()) {
					for (DataDependency dep : DataDepEventHandler.instanceOf().getLastDataDependencies()) {
						System.out.println(" ---> " + dep.getFieldName() + " with " + dep.getSourceTest() + " "
								+ dep.getTargetTest());
						// System.out.println(" ---> " + dep.getFieldName() +
						// "(" + dep.getFieldOwner() + "."
						// + dep.getFieldName() + ")");
					}
				}
			}

		});
		for (Request request : parsedInputs.getTestRequests()) {
			core.run(request);
			totalTests++;
		}

		// Output failed tests
		for (Failure failure : failedTests.values()) {
			// FAILING TEST:
			// testNullInputConstructor(crystal.model.RelationshipTest)
			System.out.println("FAILING TEST " + failure.getDescription());
			failure.getException().printStackTrace(System.out);
		}

		dataDependencies.addAll(DataDepEventHandler.instanceOf().getDataDependencies());
		//
		System.out.println(
				"Found " + dataDependencies.size() + " data dependencies (" + compactDataDependencies.size() + ")");

		File outputFile = parsedInputs.getOutputFile();

		// TODO Only if enabled print out details
		System.out.println("Writing dependencies to file " + outputFile.getAbsolutePath());

		// Abstract and remove duplicates
		// Remove duplicated

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));) {

			// if (parsedInputs.isCompact()) {
			List<CompactDataDependency> orderedDeps = new ArrayList<CompactDataDependency>(compactDataDependencies);
			Collections.sort(orderedDeps, new Comparator<CompactDataDependency>() {
				@Override
				public int compare(CompactDataDependency o1, CompactDataDependency o2) {
					if (o1.getTargetTest().compareTo(o2.getTargetTest()) == 0) {
						return o1.getSourceTest().compareTo(o2.getSourceTest());
					} else {
						return o1.getTargetTest().compareTo(o2.getTargetTest());
					}
				}
			});

			for (CompactDataDependency d : orderedDeps) {
				bw.write(d.getTargetTest() + "," + d.getSourceTest() + "\n");
			}
			// } else {
			// for (DataDependency d : dataDependencies) {
			// bw.write(d.getTargetTest() + "," + d.getSourceTest() + "," +
			// d.getFieldOwner() + ","
			// + d.getFieldName() + "\n");
			// }
			// }
		}

		System.out.println("########################################");
		System.out.println("\tSuccessful Tests:\t" + (totalTests - failedTests.size()));
		System.out.println("\tFailing Tests:\t\t" + failedTests.size());
		System.out.println("\tAll Tests:\t\t" + totalTests);
		System.out.println("########################################");

		// Explicit shutdown ?!
		System.exit(0);
	}
}
