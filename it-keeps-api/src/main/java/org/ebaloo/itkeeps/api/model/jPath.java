package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("ALL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class jPath extends jBaseChildAcl {

	public static final String PARENT_PATH = jBase.PARENT;
	public static final String CHILDS_PATHS = jBase.CHILDS;

	
	public jPath() {
		super();
	}
	
	
	// PARENT_GROUP
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<jBaseLight> parent = Optional.empty();
	
	@JsonProperty(PARENT_PATH)
	public jBaseLight getParent() {
		return parent.orElse(null);
	}

	@JsonProperty(PARENT_PATH)
	public void setParent(jBaseLight value) {
		this.parent = Optional.ofNullable(value);
	}

	@JsonIgnore
	public boolean isPresentParent() {
		return this.parent.isPresent();
	}
	
	
	// CHILD_GROUPS
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<List<jBaseLight>> childs = Optional.empty();
	
	@JsonProperty(CHILDS_PATHS)
	public final List<jBaseLight> getChilds() {
		return this.childs.orElse(new ArrayList<jBaseLight>());
	}

	@JsonProperty(CHILDS_PATHS)
	public final void setChilds(List<jBaseLight> value) {
		childs = Optional.ofNullable(value);
	}

	@JsonIgnore
	public final boolean isPresentChilds() {
		return this.childs.isPresent();
	}
	
}
