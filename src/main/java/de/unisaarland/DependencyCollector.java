
package de.unisaarland;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

		// TODO Check PreparingTestListener
		core.addListener(new RunListener() {
			// TODO Use matchers and patterns
			@Override
			public void testStarted(Description description) throws Exception {
				String[] d = description.toString().replace("(", " ").replace(")", "").split(" ");
				String testClass = d[1];
				String testMethod = d[0];

				System.out.println("Start " + testClass + "." + testMethod);
				// System.out.println("testStarted() " + testClass + "." +
				// testMethod);

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
					System.out.println("Fail >>>> " + testClass + "." + testMethod);
					// failedTests.get(description).getException().getCause().printStackTrace(System.out);
				} else {
					System.out.println("End " + testClass + "." + testMethod);

				}
				DataDepEventHandler.instanceOf().afterTestExecution();
			}

		});
		for (Request request : parsedInputs.getTestRequests()) {
			core.run(request);
			// TODO What about exceptions inside the test ?!
		}

		///
		List<DataDependency> dataDependencies = DataDepEventHandler.instanceOf().getDataDependencies();
		Set<CompactDataDependency> compactDataDependencies = new LinkedHashSet<CompactDataDependency>(dataDependencies);
		//
		System.out.println(
				"Found " + dataDependencies.size() + " data dependencies (" + compactDataDependencies.size() + ")");

		File outputFile = parsedInputs.getOutputFile();

		// TODO Only if enabled print out details
		System.out.println("Writing dependencies to file " + outputFile.getAbsolutePath());

		// Abstract and remove duplicates
		// Remove duplicated
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));) {

			if (parsedInputs.isCompact()) {

				for (CompactDataDependency d : compactDataDependencies) {
					bw.write(d.getSourceTest() + "," + d.getTargetTest() + "\n");
				}
			} else {
				for (DataDependency d : dataDependencies) {
					bw.write(d.getSourceTest() + "," + d.getTargetTest() + "," + d.getFieldOwner() + ","
							+ d.getFieldName() + "\n");
				}
			}
		}
	}
}
