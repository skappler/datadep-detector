package de.unisaarland.analysis;

public class CompactDataDependency {

	private String sourceTest;
	private String targetTest;

	public CompactDataDependency(String sourceTest, String targetTest) {
		this.sourceTest = sourceTest;
		this.targetTest = targetTest;
	}

	public String getSourceTest() {
		return sourceTest;
	}

	public String getTargetTest() {
		return targetTest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceTest == null) ? 0 : sourceTest.hashCode());
		result = prime * result + ((targetTest == null) ? 0 : targetTest.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompactDataDependency other = (CompactDataDependency) obj;
		if (sourceTest == null) {
			if (other.sourceTest != null)
				return false;
		} else if (!sourceTest.equals(other.sourceTest))
			return false;
		if (targetTest == null) {
			if (other.targetTest != null)
				return false;
		} else if (!targetTest.equals(other.targetTest))
			return false;
		return true;
	}

}
