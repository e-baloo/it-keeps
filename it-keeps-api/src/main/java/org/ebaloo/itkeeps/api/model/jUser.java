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

//	public static final String USER_ID = "userId";
	public static final String ROLE = "role";
	public static final String IN_GROUPS = "inGroups";

	
	public jUser() {
		super();
	}
	
	
	/*
	// ID
	
	@JsonIgnore
	private Optional<String> userId = Optional.empty();
	
	@JsonProperty(USER_ID)
	public String getUserId() {
		return userId.orElse(null);
	}

	@JsonProperty(USER_ID)
	public void setUserId(String value) {
		this.userId = Optional.of(value == null ? "" : value);
	}

	@JsonIgnore
	public boolean isPresentUserId() {
		return this.userId.isPresent();
	}
	*/
	
	// ROLE
	
	@JsonIgnore
	private Optional<enSecurityRole> role = Optional.empty();
	
	@JsonIgnore
	public final enSecurityRole getRole() {
		return this.role.orElse(null);
	}

	@JsonIgnore
	public final void setRole(enSecurityRole value) {
		role = Optional.of(value);
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

	/*
	// PASSWORD
	
	@JsonIgnore
	private Optional<String> password = Optional.empty();
	
	@JsonProperty(PASSWORD)
	public final String getPassword() {
		return this.password.orElse(null);
	}

	@JsonProperty(PASSWORD)
	public final void setPassword(String value) {
		password = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isPresentPassword() {
		return this.password.isPresent();
	}
	*/

	// IN_GROUPS
	
	@JsonIgnore
	private Optional<List<jBaseLight>> inGroups = Optional.empty();
	
	@JsonProperty(IN_GROUPS)
	public final List<jBaseLight> getInGroups() {
		return this.inGroups.orElse(new ArrayList<jBaseLight>());
	}

	@JsonProperty(IN_GROUPS)
	public final void setInGroups(List<jBaseLight> value) {
		if(value == null)
			inGroups = Optional.of(new ArrayList<jBaseLight>());
		else
			inGroups = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isPresentInGroup() {
		return this.inGroups.isPresent();
	}
}
