package de.unisaarland.analysis;

public class DataDependency extends CompactDataDependency {

	private String fieldName;
	private String fieldOwner;
	private int sourceTestID;
	private int targetTestID;

	// Use a factory method ?
	public DataDependency(String fieldOwner, String fieldName, //
			int sourceTestID, int targetTestID, String sourceTest, String targetTest) {
		super(sourceTest, targetTest);
		this.fieldName = fieldName;
		this.fieldOwner = fieldOwner;
		this.sourceTestID = sourceTestID;
		this.targetTestID = targetTestID;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldOwner() {
		return fieldOwner;
	}

	@Override
	public String toString() {
		return getTargetTest() + "(" + targetTestID + ")" + " dependsOn " + getSourceTest() + "(" + sourceTestID
				+ ") on " + getFieldOwner() + "." + getFieldName();
	}

}