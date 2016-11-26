package org.ebaloo.itkeeps;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

public class Rid {

	public static Rid NULL = new Rid();

	public static final String RDI_NAME = "@rid";

	public static final void check(final String rid) {
		if (!Rid.is(rid))
			throw new RuntimeException(String.format("'%s' is not valid %s", rid, Rid.class.getSimpleName()));
	}

	public static final boolean is(final String orid) {
		return orid == null ? false : orid.matches("#\\d+:\\d+");
	}

	@JsonIgnore
	private String rid;

	private Rid() {
		this.rid = null;
	}

	public Rid(final String rid) {
		this.set(rid);
	}

	@JsonValue
	public String get() {
		return this.rid;
	}

	@JsonValue
	private void set(final String rid) {
		Rid.check(rid);
		this.rid = rid;
	}
	
	public String toString() {
		return this.get();
	}
}