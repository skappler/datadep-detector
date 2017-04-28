package de.unisaarland;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.runner.Request;

import com.lexicalscope.jewel.cli.Option;
import com.lexicalscope.jewel.cli.Unparsed;

public class ParsingInterface {

	//
	private File testList;
	// Manually specified tests, mostly for debugging
	private List<String> manuallySpecifiedTests = new ArrayList<String>();

	private File outputFile;

	//
	private boolean compact;

	// Test classes are on the same classpath as this DepDetector
	// File is formatted with QN of test method
	@Option(longName = "output-file", defaultValue = "deps.csv")
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	@Option(longName = "compact")
	public void setCompact(boolean compact) {
		this.compact = compact;
	}

	// // TODO Comma separated value ?... not sure how to specify this
	// @Option(longName = "tests", defaultToNull = true, pattern="\\w[,\\w]*")
	// public void setCompact(List<String> tests) {
	// this.manuallySpecifiedTests.addAll(tests);
	// }

	// TODO Comma separated value ?... not sure how to specify this
	@Option(longName = "tests", defaultToNull = true)
	public void setCompact(String testsList) {
		if (testsList != null) {
			this.manuallySpecifiedTests.addAll(Arrays.asList(testsList.split(",")));
		}
	}

	@Unparsed(defaultToNull = true)
	public void setTestListFile(File testList) {
		this.testList = testList;
	}

	public boolean isCompact() {
		return compact;
	}

	// Do the actual parsing.
	// TODO Strict option to fail if something is wrong
	public List<Request> getTestRequests() throws FileNotFoundException, IOException, ClassNotFoundException {
		List<Request> tests = new ArrayList<Request>();

		// Process first the manually defined tests
		for (String line : manuallySpecifiedTests) {
			String className = line.substring(0, line.lastIndexOf("."));
			String methodName = line.substring(line.lastIndexOf(".") + 1, line.length());
			tests.add(Request.method(Class.forName(className), methodName));
		}

		if (this.testList != null) {
			try (FileInputStream inputStream = new FileInputStream(this.testList);
					Scanner sc = new Scanner(inputStream, "UTF-8");) {

				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					String className = line.substring(0, line.lastIndexOf("."));
					String methodName = line.substring(line.lastIndexOf(".") + 1, line.length());
					tests.add(Request.method(Class.forName(className), methodName));
					// System.out.println("ParsingInterface.getTestRequests()
					// Added
					// " + className + " " + methodName);
				}
				// note that Scanner suppresses exceptions
				if (sc.ioException() != null) {
					throw sc.ioException();
				}
			}
		}

		return tests;
	}

	public File getOutputFile() {
		return this.outputFile;
	}

}
