package org.ebaloo.itkeeps.api.model;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class jBaseChildAcl extends jBaseStandard {

	public static final String ACLS = jAcl.ACLs;


	@JsonIgnore
	private Optional<List<jBaseLight>> acls = Optional.empty();


	public jBaseChildAcl() {
		super();
	}

	@JsonProperty(ACLS)
	public final List<jBaseLight> getAcls() {
		return this.acls.isPresent() ? this.acls.get() : null;
	}

	@JsonIgnore
	public final boolean isPresentAcls() {
		return this.acls.isPresent();
	}

	@JsonProperty(ACLS)
	public final void setAcls(List<jBaseLight> value) {
		acls = Optional.ofNullable(value);
	}

}
