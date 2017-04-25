package de.unisaarland.analysis;

public class DataDependency {

	private String fieldName;
	private String fieldOwner;
	private int sourceTestID;
	private int targetTestID;
	private String sourceTest;
	private String targetTest;

	// Use a factory method ?
	public DataDependency(String fieldOwner, String fieldName, //
			int sourceTestID, int targetTestID, String sourceTest, String targetTest) {
		this.fieldName = fieldName;
		this.fieldOwner = fieldOwner;
		this.sourceTest = sourceTest;
		this.sourceTestID = sourceTestID;
		this.targetTest = targetTest;
		this.targetTestID = targetTestID;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldOwner() {
		return fieldOwner;
	}

	public String getSourceTest() {
		return sourceTest;
	}

	public String getTargetTest() {
		return targetTest;
	}

	@Override
	public String toString() {
		return getTargetTest() + "(" + targetTestID + ")" + " dependsOn " + getSourceTest() + "(" + sourceTestID
				+ ") on " + getFieldOwner() + "." + getFieldName();
	}

}