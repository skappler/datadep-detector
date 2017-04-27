
package de.unisaarland;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.lexicalscope.jewel.cli.CliFactory;

import de.unisaarland.analysis.DataDepEventHandler;
import de.unisaarland.analysis.DataDependency;

public class DependencyCollector {

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		System.out.println("DependencyCollector.main()");
		ParsingInterface parsedInputs = CliFactory.parseArgumentsUsingInstance(new ParsingInterface(), args);
		JUnitCore core = new JUnitCore();
		// TODO Check PreparingTestListener
		core.addListener(new RunListener() {
			// TODO Use matchers and patterns
			@Override
			public void testStarted(Description description) throws Exception {
				System.out.println("testStarted() " + description);
				String[] d = description.toString().replace("(", " ").replace(")", "").split(" ");
				String testClass = d[1];
				String testMethod = d[0];

				// System.out.println("testStarted() " + testClass + "." +
				// testMethod);

				DataDepEventHandler.instanceOf().beforeTestExecution(testClass, testMethod);
			}

			@Override
			public void testFailure(Failure failure) throws Exception {
				System.out.println("Failed " + failure);
			}

			@Override
			public void testFinished(Description description) throws Exception {
				System.out.println("testFinished() " + description);
				// String[] d = description.toString().replace("(", "
				// ").replace(")", "").split(" ");
				// String testClass = d[1];
				// String testMethod = d[0];
				//
				// System.out.println("testFinished()" + testClass + "." +
				// testMethod);
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
				
				System.out.println(d.getSourceTest() + " --> " + d.getTargetTest() + " on " + d.getFieldOwner() + "."
						+ d.getFieldName());
				
				bw.write(d.getSourceTest() + "," + d.getTargetTest() + "\n");
			}
		}
	}
}
