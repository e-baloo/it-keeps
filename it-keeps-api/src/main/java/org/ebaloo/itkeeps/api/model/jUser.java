package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;

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
	private Optional<enAclRole> role = Optional.empty();
	
	@JsonIgnore
	public final enAclRole getRole() {
		return this.role.orElse(null);
	}

	@JsonIgnore
	public final void setRole(enAclRole value) {
		role = Optional.ofNullable(value);
	}

	@JsonProperty(ROLE)
	public final String _getRole() {
		if(isPresentRole())
			return this.role.get().name();
		else
			return null;
	}

	@JsonProperty(ROLE)
	public final void _setRole(String value) {
		role = Optional.of(enAclRole.valueOf(value));
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
	
	// ACL_ADMIN_TYPE
	
	public static final String ACL_ADMIN = jAcl.ACL_ADMIN;
	
	@JsonIgnore
	private Optional<List<enAclAdmin>> aclAdmin = Optional.empty();
	
	@JsonIgnore
	public final List<enAclAdmin> getAclAdmin() {
		return this.aclAdmin.orElse(null);
	}

	@JsonIgnore
	public final void setAclAdmin(List<enAclAdmin> value) {
		aclAdmin = Optional.ofNullable(value);
	}

	@JsonProperty(ACL_ADMIN)
	public final List<String> _getAclAdmin() {
		if(aclAdmin.isPresent())
			return this.aclAdmin.get().stream().map(e -> e.name()).collect(Collectors.toList());
		else
			return null;
	}
	
	@JsonProperty(ACL_ADMIN)
	public final void _setAclAdmin(List<String> value) {
		if(value == null)
			aclAdmin = Optional.empty();
		else
			aclAdmin = Optional.of(value.stream().map(e -> enAclAdmin.valueOf(e)).collect(Collectors.toList()));
	}

	@JsonIgnore
	public boolean isPresentAclAdmin() {
		return aclAdmin.isPresent();
	}
}