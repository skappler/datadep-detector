package de.unisaarland;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;

import de.unisaarland.analysis.DataDepEventHandler;
import de.unisaarland.analysis.DataDependency;

public class DependencyCollector {

	public static void main(String[] args) {
		try {
			ParsingInterface parsedInputs = CliFactory.parseArgumentsUsingInstance(new ParsingInterface(), args);
			JUnitCore core = new JUnitCore();
			// TODO Check PreparingTestListener
			core.addListener(new RunListener() {
				// TODO Use matchers and patterns
				@Override
				public void testStarted(Description description) throws Exception {
					// initializationError(crystal.client.ClientPreferencesTest)
					// Hack by cannot do otherwise
					String[] d = description.toString().replace("(", " ").replace(")", "").split(" ");
					String testClass = d[1];
					String testMethod = d[0];

					System.out.println("testStarted() " + testClass + "." + testMethod);

					DataDepEventHandler.instanceOf().beforeTestExecution(testClass, testMethod);
				}

				@Override
				public void testFailure(Failure failure) throws Exception {
					System.out.println("Failed " + failure);
				}

				@Override
				public void testFinished(Description description) throws Exception {
					String[] d = description.toString().replace("(", " ").replace(")", "").split(" ");
					String testClass = d[1];
					String testMethod = d[0];

					System.out.println("testFinished()" + testClass + "." + testMethod);
					DataDepEventHandler.instanceOf().afterTestExecution();
				}

			});
			for (Request request : parsedInputs.getTestRequests()) {
				core.run(request);
				// TODO What about exceptions inside the test ?!
			}
			List<DataDependency> dataDependencies = DataDepEventHandler.instanceOf().getDataDependencies();
			System.out.println("Found " + dataDependencies.size() + " data dependencies");

			File outputFile = parsedInputs.getOutputFile();
			System.out.println("Writing dependencies to file " + outputFile.getAbsolutePath());
			//
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));) {
				for (DataDependency d : dataDependencies) {
					bw.write(d.getSourceTest() + "," + d.getTargetTest() + "\n");
				}
			}

			// String timePath = System.getProperty("time-output",
			// "exectimes.csv");
			// bw = new BufferedWriter(new FileWriter(new File(timePath)));
			// for (String s : execTimes.keySet()) {
			// bw.write(s + "," + execTimes.get(s) + "," +
			// execTimesWithHeapWalk.get(s) + "\n");
			// }
			// bw.close();
			//
			// String testResultsPath = System.getProperty("test-results",
			// "test-results.csv");
			// bw = new BufferedWriter(new FileWriter(new
			// File(testResultsPath)));
			// for (String s : results.keySet()) {
			// bw.write(s + "," + results.get(s) + "\n");
			// }
			// bw.close();

		} catch (ArgumentValidationException | ClassNotFoundException |

				IOException e) {
			e.printStackTrace();
		}
	}
}
