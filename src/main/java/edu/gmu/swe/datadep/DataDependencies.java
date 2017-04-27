package edu.gmu.swe.datadep;

import java.util.HashSet;
import java.util.Set;

public class DataDependencies {
	private static final long serialVersionUID = -3190799049403762641L;

	private static DataDependencies INSTANCE;

	public synchronized static DataDependencies getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataDependencies();
		}
		return INSTANCE;
	}

	private Set<DataDependency> depsData;

	// Not thread safe ... - Maybe static
	public Set<DataDependency> dump() {
		Set<DataDependency> copyOf = new HashSet<DataDependency>(depsData.size());
		for (DataDependency d : depsData) {
			copyOf.add(new DataDependency(d.getFirst(), d.getSecond(), d.getThird()));
		}
		return copyOf;
	}

	public DataDependencies() {
		depsData = new HashSet<>();
	}

	public synchronized void add(int s, int d, String depName) {
		depsData.add(new DataDependency(depName, s, d));
	}

	// Not sure why this says 9 ...
	public synchronized int size() {
		return depsData.size();
	}

	public void printAll() {
		for (DataDependency d : depsData) {
			System.out.println(
					"DataDependencies.printAll() " + d.getFirst() + " " + d.getSecond() + " ->" + d.getThird());
		}
	}

	public void clearAll() {
		depsData.clear();

	}

}
