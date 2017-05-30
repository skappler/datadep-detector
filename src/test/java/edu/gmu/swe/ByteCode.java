package edu.gmu.swe;

import edu.gmu.swe.datadep.DependencyInfo;

public class ByteCode {

	private String o;
	private DependencyInfo _o__DEPENDENCY_INFO;

	private void checkNullAndSkip() {
		if (o != null) {
			o.toString();
			_o__DEPENDENCY_INFO.read(o);
		}
		System.out.println("ByteCode.checkNullAndSkip()");
	}
}
