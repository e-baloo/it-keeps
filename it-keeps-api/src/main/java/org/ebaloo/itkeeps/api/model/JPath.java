package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JPath extends JBaseStandard{

	public static final String PARENT_PATH = JBase.PARENT;
	public static final String CHILDS_PATHS = JBase.CHILDS;

	
	public JPath() {
		super();
	}
	
	
	// PARENT_GROUP
	
	@JsonIgnore
	private Optional<JBaseLight> parent = Optional.empty();
	
	@JsonProperty(PARENT_PATH)
	public JBaseLight getParent() {
		return parent.orElse(null);
	}

	@JsonProperty(PARENT_PATH)
	public void setParent(JBaseLight value) {
		
		if(value == null)
			value = new JBaseLight();
		
		this.parent = Optional.of(value);
	}

	@JsonIgnore
	public boolean isPresentParent() {
		return this.parent.isPresent();
	}
	
	
	// CHILD_GROUPS
	
	@JsonIgnore
	private Optional<List<JBaseLight>> childs = Optional.empty();
	
	@JsonProperty(CHILDS_PATHS)
	public final List<JBaseLight> getChilds() {
		return this.childs.orElse(new ArrayList<JBaseLight>());
	}

	@JsonProperty(CHILDS_PATHS)
	public final void setChilds(List<JBaseLight> value) {
		if(value == null)
			childs = Optional.of(new ArrayList<JBaseLight>());
		else
			childs = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isPresentChilds() {
		return this.childs.isPresent();
	}
	
}
