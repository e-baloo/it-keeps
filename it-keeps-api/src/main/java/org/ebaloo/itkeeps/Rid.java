package org.ebaloo.itkeeps;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rid {

    public static final String RDI_NAME = "id";
    private static final String MATCH_FULL = "^#(\\d+):(\\d+)$";
    private static final String MATCH_SIMPLE = "^c(\\d+)i(\\d+)$";
    private static final String FORMAT_RID_FULL = "#%s:%s";
    private static final String FORMAT_RID_SIMPLE = "c%si%s";
    public static Rid NULL = new Rid();
    private static Pattern PATTERN_FULL = Pattern.compile(MATCH_FULL);
    private static Pattern PATTERN_SIMPLE = Pattern.compile(MATCH_SIMPLE);
    @JsonIgnore
    private int cluster = -1;
    @JsonIgnore
    private int id = -1;

    private Rid() {

	}

	public Rid(final String rid) {
		this.set(rid);
	}

    private static void check(final String rid) {
        if (!Rid.is(rid))
			throw new RuntimeException(String.format("'%s' is not valid %s", rid, Rid.class.getSimpleName()));
	}

    public static boolean is(final String rid) {
        return rid != null && (PATTERN_FULL.matcher(rid).matches() || PATTERN_SIMPLE.matcher(rid).matches());
    }


    @JsonValue
	public String get() {
        return this.getSimple();
    }

    @JsonIgnore
    public String getSimple() {
        return String.format(FORMAT_RID_SIMPLE, this.cluster, this.id);
    }

    @JsonIgnore
    public String getFull() {
        return String.format(FORMAT_RID_FULL, this.cluster, this.id);
    }


    @JsonValue
    private void set(final String rid) {
		Rid.check(rid);

        Matcher matcher;

        if (PATTERN_FULL.matcher(rid).matches()) {
            matcher = PATTERN_FULL.matcher(rid);
        } else {
            matcher = PATTERN_SIMPLE.matcher(rid);
        }

        matcher.matches();
        this.cluster = Integer.parseInt(matcher.group(1));
        this.id = Integer.parseInt(matcher.group(2));
    }
	
	public String toString() {
        return this.getSimple();
    }
	
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Rid && this.cluster == ((Rid) obj).cluster && this.id == ((Rid) obj).id;
    }

}