package org.ebaloo.itkeeps.api;

public enum AclDataType {

	DELEGATE(1),
	NO_DELEGATE(-1);

	
	private final int value;
	
	private AclDataType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
}
