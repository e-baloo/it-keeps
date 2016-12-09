package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.enAbstract;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class jAclGroup extends jBaseStandard{

	public static final String PARENTS = jBase.PARENTS;
	public static final String CHILD = jBase.CHILD;

	
	public jAclGroup() {
		super();
	}
	
	
	// CHILD
	
	@JsonIgnore
	private jBaseLight child = null;
	
	@JsonProperty(CHILD)
	public jBaseLight getChild() {
		return child;
	}

	@JsonProperty(CHILD)
	public void setChild(jBaseLight value) {
		this.child = value;
	}

	
	// PARENTS
	
	@JsonIgnore
	private List<jBaseLight> parents = new ArrayList<>();
	
	@JsonProperty(PARENTS)
	public final List<jBaseLight> getParents() {
		return this.parents;
	}

	@JsonProperty(PARENTS)
	public final void setParents(List<jBaseLight> value) {
		parents = value == null ? new ArrayList<>() : value;
	}


	
	// ACL_ADMIN
	
	public static final String ACL_ADMIN = jAcl.ACL_ADMINS;
	
	@JsonIgnore
	private List<enAclAdmin> aclAdmin = new ArrayList<>();
	
	@JsonIgnore
	public final List<enAclAdmin> getAclAdmin() {
		return this.aclAdmin;
	}

	@JsonIgnore
	public final void setAclAdmin(List<enAclAdmin> value) {
		aclAdmin = value == null ? new ArrayList<>() : value;
	}

	@JsonProperty(ACL_ADMIN)
	public final List<String> _getAclAdmin() {
		return this.aclAdmin.stream().map(enAbstract::name).collect(Collectors.toList());
	}
	
	@JsonProperty(ACL_ADMIN)
	public final void _setAclAdmin(List<String> value) {
		aclAdmin = value == null ? new ArrayList<>() : value.stream().map(enAclAdmin::valueOf).collect(Collectors.toList());
	}

	
	// ACL_DATA
	
	public static final String ACL_DATA = jAcl.ACL_DATA;
	
	@JsonIgnore
	private List<enAclData> aclData = new ArrayList<>();
	
	@JsonIgnore
	public final List<enAclData> getAclData() {
		return this.aclData;
	}

	@JsonIgnore
	public final void setAclData(List<enAclData> value) {
		aclData = value == null ? new ArrayList<>() : value;
	}

	@JsonProperty(ACL_DATA)
	public final List<String> _getAclData() {
		return this.aclData.stream().map(enAbstract::name).collect(Collectors.toList());
	}
	
	@JsonProperty(ACL_DATA)
	public final void _setAclData(List<String> value) {
		this.aclData = value == null ? new ArrayList<>() : value.stream().map(enAclData::valueOf).collect(Collectors.toList());
	}
}
