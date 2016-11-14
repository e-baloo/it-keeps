package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JGroup extends JBaseStandard{

	public static final String PARENT_GROUP = "parentGroup";
	public static final String CHILD_GROUP = "childGroup";

	
	public JGroup() {
		super();
	}
	
	
	// PARENT_GROUP
	
	@JsonIgnore
	private Optional<JBaseLight> parentGroup = Optional.empty();
	
	@JsonProperty(PARENT_GROUP)
	public JBaseLight getParentGroup() {
		return parentGroup.orElse(null);
	}

	@JsonProperty(PARENT_GROUP)
	public void setParentGroup(JBaseLight value) {
		
		if(value == null)
			value = new JBaseLight();
		
		this.parentGroup = Optional.of(value);
	}

	@JsonIgnore
	public boolean isPresentParentGroup() {
		return this.parentGroup.isPresent();
	}
	
	
	// CHILD_GROUP
	
	@JsonIgnore
	private Optional<List<JBaseLight>> childGroup = Optional.empty();
	
	@JsonProperty(CHILD_GROUP)
	public final List<JBaseLight> getChildGroup() {
		return this.childGroup.orElse(new ArrayList<JBaseLight>());
	}

	@JsonProperty(CHILD_GROUP)
	public final void setChildGroup(List<JBaseLight> value) {
		if(value == null)
			childGroup = Optional.of(new ArrayList<JBaseLight>());
		else
			childGroup = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isChildGroup() {
		return this.childGroup.isPresent();
	}
	
}
