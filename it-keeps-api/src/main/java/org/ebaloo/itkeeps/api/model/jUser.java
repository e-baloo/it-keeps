package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enSecurityRole;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class jUser extends jBaseStandard{

	public static final String ROLE = "role";
	public static final String IN_GROUPS = "inGroups";

	
	public jUser() {
		super();
	}
	
	
	
	// ROLE
	
	@JsonIgnore
	private Optional<enSecurityRole> role = Optional.empty();
	
	@JsonIgnore
	public final enSecurityRole getRole() {
		return this.role.orElse(null);
	}

	@JsonIgnore
	public final void setRole(enSecurityRole value) {
		role = Optional.ofNullable(value);
	}

	@JsonProperty(ROLE)
	public final String _getRole() {
		if(isPresentRole())
			return this.role.get().toString();
		else
			return null;
	}

	@JsonProperty(ROLE)
	public final void _setRole(String value) {
		role = Optional.of(enSecurityRole.valueOf(value));
	}

	@JsonIgnore
	public final boolean isPresentRole() {
		return this.role.isPresent();
	}


	// IN_GROUPS
	
	@JsonIgnore
	private Optional<List<jBaseLight>> inGroups = Optional.empty();
	
	@JsonProperty(IN_GROUPS)
	public final List<jBaseLight> getInGroups() {
		return this.inGroups.orElse(new ArrayList<jBaseLight>());
	}

	@JsonProperty(IN_GROUPS)
	public final void setInGroups(List<jBaseLight> value) {
		inGroups = Optional.ofNullable(value);
	}

	@JsonIgnore
	public final boolean isPresentInGroup() {
		return this.inGroups.isPresent();
	}
}
