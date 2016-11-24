package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class jSecurityGroup extends jBaseStandard{

	public static final String PARENT_GROUP = jBase.PARENT;
	public static final String CHILDS_GROUPS = jBase.CHILDS;

	
	public jSecurityGroup() {
		super();
	}
	
	
	// PARENT_GROUP
	
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
	
	@JsonIgnore
	private Optional<List<jBaseLight>> childGroups = Optional.empty();
	
	@JsonProperty(CHILDS_GROUPS)
	public final List<jBaseLight> getChilds() {
		return this.childGroups.orElse(new ArrayList<jBaseLight>());
	}

	@JsonProperty(CHILDS_GROUPS)
	public final void setChilds(List<jBaseLight> value) {
		childGroups = Optional.ofNullable(value);
	}

	@JsonIgnore
	public final boolean isPresentChilds() {
		return this.childGroups.isPresent();
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
