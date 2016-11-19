package org.ebaloo.itkeeps.api.enumeration;

public abstract class EnumAbstract<K> {

	private final String _name;
	private final int _ordinal;
	private final K _value;

	protected EnumAbstract(String name, int ordinal, K value) {
		this._name = name;
		this._ordinal = ordinal;
		this._value = value;
	}

	public K value() {
		return this._value;
	}

	public int ordinal() {
		return this._ordinal;
	}

	public String name() {
		return this._name;
	}


	
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;

		if (!this.getClass().isInstance(other)) {
			return false;
		}

		return this == other;
	}
	
	
	
	public String toString() {
		return String.format("[%s|%s|%s]", this._name, this._ordinal, this.value());
	}
	
	
}
