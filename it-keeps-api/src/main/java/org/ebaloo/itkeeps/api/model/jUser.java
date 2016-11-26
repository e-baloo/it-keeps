package org.ebaloo.itkeeps.api.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class jUser extends jBaseStandard {

	public static final String ACL_ADMIN = jAcl.ACL_ADMIN;
	public static final String ACL_GROUPS = "aclgroups";
	public static final String CREDENTIALS = "credentials";
	public static final String GROUPS = "groups";
	public static final String ROLE = "role";


	@JsonIgnore
	private Optional<List<enAclAdmin>> aclAdmin = Optional.empty();

	@JsonIgnore
	private Optional<List<jBaseLight>> aclGroups = Optional.empty();

	@JsonIgnore
	private Optional<List<jBaseLight>> credentials = Optional.empty();

	@JsonIgnore
	private Optional<List<jBaseLight>> groups = Optional.empty();

	@JsonIgnore
	private Optional<enAclRole> role = Optional.empty();

	public jUser() {
		super();
	}

	@JsonProperty(ACL_ADMIN)
	public final List<String> _getAclAdmin() {
		return this.aclAdmin.isPresent() ? this.aclAdmin.get().stream().map(e -> e.name()).collect(Collectors.toList())
				: null;
	}

	@JsonProperty(ROLE)
	public final String _getRole() {
		return this.role.isPresent() ? this.role.get().name() : null;
	}

	@JsonProperty(ACL_ADMIN)
	public final void _setAclAdmin(List<String> value) {
		if (value == null)
			this.aclAdmin = Optional.empty();
		else
			this.aclAdmin = Optional.of(value.stream().map(e -> enAclAdmin.valueOf(e)).collect(Collectors.toList()));
	}

	@JsonProperty(ROLE)
	public final void _setRole(String value) {
		if (value == null)
			role = Optional.empty();
		else
			role = Optional.of(enAclRole.valueOf(value));
	}

	@JsonIgnore
	public final List<enAclAdmin> getAclAdmin() {
		return this.aclAdmin.isPresent() ? this.aclAdmin.get() : null;
	}

	@JsonProperty(ACL_GROUPS)
	public final List<jBaseLight> getAclGroups() {
		return this.aclGroups.isPresent() ? this.aclGroups.get() : null;
	}

	@JsonProperty(CREDENTIALS)
	public final List<jBaseLight> getCredentials() {
		return this.credentials.isPresent() ? this.credentials.get() : null;
	}

	@JsonProperty(GROUPS)
	public final List<jBaseLight> getGroups() {
		return this.groups.isPresent() ? this.groups.get() : null;
	}

	@JsonIgnore
	public final enAclRole getRole() {
		return this.role.isPresent() ? this.role.get() : null;
	}

	@JsonIgnore
	public boolean isPresentAclAdmin() {
		return aclAdmin.isPresent();
	}

	@JsonIgnore
	public final boolean isPresentAclGroups() {
		return this.aclGroups.isPresent();
	}

	@JsonIgnore
	public final boolean isPresentCredentials() {
		return this.credentials.isPresent();
	}

	@JsonIgnore
	public final boolean isPresentGroups() {
		return this.groups.isPresent();
	}

	@JsonIgnore
	public final boolean isPresentRole() {
		return this.role.isPresent();
	}

	@JsonIgnore
	public final void setAclAdmin(List<enAclAdmin> value) {
		this.aclAdmin = Optional.ofNullable(value);
	}

	@JsonProperty(ACL_GROUPS)
	public final void setAclGroups(List<jBaseLight> value) {
		aclGroups = Optional.ofNullable(value);
	}

	@JsonProperty(CREDENTIALS)
	public final void setCredentials(List<jBaseLight> value) {
		credentials = Optional.ofNullable(value);
	}

	@JsonProperty(GROUPS)
	public final void setGroups(List<jBaseLight> value) {
		groups = Optional.ofNullable(value);
	}

	@JsonIgnore
	public final void setRole(enAclRole value) {
		this.role = Optional.ofNullable(value);
	}
}
