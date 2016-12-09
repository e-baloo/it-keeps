package org.ebaloo.itkeeps;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

public class Rid {

	public static final String RDI_NAME = "@rid";
	public static Rid NULL = new Rid();
	@JsonIgnore
	private String rid;

	private Rid() {
		this.rid = null;
	}

	public Rid(final String rid) {
		this.set(rid);
	}

	public static void check(final String rid) {
		if (!Rid.is(rid))
			throw new RuntimeException(String.format("'%s' is not valid %s", rid, Rid.class.getSimpleName()));
	}

	public static boolean is(final String orid) {
		return orid != null && orid.matches("#\\d+:\\d+");
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
	
    public boolean equals(Object obj) {
		return obj != null && obj instanceof Rid && this.get().equals(((Rid) obj).get());
	}

}