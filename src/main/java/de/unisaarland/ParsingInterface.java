package de.unisaarland;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.runner.Request;

import com.lexicalscope.jewel.cli.Option;
import com.lexicalscope.jewel.cli.Unparsed;

public class ParsingInterface {

	private File testList;
	private File outputFile;

	// Test classes are on the same classpath as this DepDetector
	// File is formatted with QN of test method
	@Option(longName = "output-file", defaultValue = "deps.csv")
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	@Unparsed
	public void setTestListFile(File testList) {
		this.testList = testList;
	}

	// Do the actual parsing.
	// TODO Strict option to fail if something is wrong
	public List<Request> getTestRequests() throws FileNotFoundException, IOException, ClassNotFoundException {
		List<Request> tests = new ArrayList<Request>();

		try (FileInputStream inputStream = new FileInputStream(this.testList);
				Scanner sc = new Scanner(inputStream, "UTF-8");) {

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String className = line.substring(0, line.lastIndexOf("."));
				String methodName = line.substring(line.lastIndexOf(".") + 1, line.length());
				tests.add(Request.method(Class.forName(className), methodName));
				System.out.println("ParsingInterface.getTestRequests() Added " + className + " " + methodName);
			}
			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		}
		return tests;
	}

	public File getOutputFile() {
		return this.outputFile;
	}

}
