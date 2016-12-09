package org.ebaloo.itkeeps.api.model;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("ALL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class jGroup extends jBaseChildAcl {

	public static final String PARENT_GROUP = jBase.PARENT;
	public static final String CHILDS_GROUPS = jBase.CHILDS;


	
	public jGroup() {
		super();
	}
	
	
	// PARENT_GROUP
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<jBaseLight> parentGroup = Optional.empty();
	
	@JsonProperty(PARENT_GROUP)
	public jBaseLight getParent() {
		return parentGroup.orElse(null);
	}

	@JsonProperty(PARENT_GROUP)
	public void setParent(jBaseLight value) {
		this.parentGroup = Optional.ofNullable(value);
	}

	@JsonIgnore
	public boolean isPresentParent() {
		return this.parentGroup.isPresent();
	}
	
	
	// CHILDS_GROUPS
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<List<jBaseLight>> childGroups = Optional.empty();
	
	@JsonProperty(CHILDS_GROUPS)
	public final List<jBaseLight> getChilds() {
		return this.childGroups.isPresent() ? this.childGroups.get() : null;
	}

	@JsonProperty(CHILDS_GROUPS)
	public final void setChilds(List<jBaseLight> value) {
		childGroups = Optional.ofNullable(value);
	}

	@JsonIgnore
	public final boolean isPresentChilds() {
		return this.childGroups.isPresent();
	}

}
