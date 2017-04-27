package edu.gmu.swe.datadep;

public class DataDependency {
	private String first;
	private Integer second;
	private Integer third;

	public DataDependency(String fieldName, int sourceTest, int destTest) {
		this.first = fieldName;
		this.second = sourceTest;
		this.third = destTest;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}

	public Integer getThird() {
		return third;
	}

	public void setThird(Integer third) {
		this.third = third;
	}
}