package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JGroup extends JBaseStandard{

	public static final String PARENT_GROUP = JBase.PARENT;
	public static final String CHILDS_GROUPS = JBase.CHILDS;

	
	public JGroup() {
		super();
	}
	
	
	// PARENT_GROUP
	
	@JsonIgnore
	private Optional<JBaseLight> parentGroup = Optional.empty();
	
	@JsonProperty(PARENT_GROUP)
	public JBaseLight getParent() {
		return parentGroup.orElse(null);
	}

	@JsonProperty(PARENT_GROUP)
	public void setParent(JBaseLight value) {
		
		if(value == null)
			value = new JBaseLight();
		
		this.parentGroup = Optional.of(value);
	}

	@JsonIgnore
	public boolean isPresentParent() {
		return this.parentGroup.isPresent();
	}
	
	
	// CHILDS_GROUPS
	
	@JsonIgnore
	private Optional<List<JBaseLight>> childGroups = Optional.empty();
	
	@JsonProperty(CHILDS_GROUPS)
	public final List<JBaseLight> getChilds() {
		return this.childGroups.orElse(new ArrayList<JBaseLight>());
	}

	@JsonProperty(CHILDS_GROUPS)
	public final void setChilds(List<JBaseLight> value) {
		if(value == null)
			childGroups = Optional.of(new ArrayList<JBaseLight>());
		else
			childGroups = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isPresentChilds() {
		return this.childGroups.isPresent();
	}
	
}
