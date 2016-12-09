package org.ebaloo.itkeeps.api.model;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("ALL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class jEntry extends jBaseChildAcl {

	public static final String PATH = "path";
	public static final String ENCRYPTED_DATA = "encryptedData";

	
	public jEntry() {
		super();
	}
	
	

	// PATH
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<jBaseLight> path = Optional.empty();
	
	@JsonProperty(PATH)
	public final jBaseLight getPath() {
		return this.path.orElse(null);
	}

	@JsonProperty(PATH)
	public final void setPath(jBaseLight value) {
		path = Optional.ofNullable(value);
	}

	@JsonIgnore
	public final boolean isPresentPath() {
		return this.path.isPresent();
	}
	
	
}
