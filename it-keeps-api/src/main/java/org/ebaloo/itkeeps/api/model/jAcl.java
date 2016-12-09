package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.enAbstract;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.enumeration.enAclOwner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class jAcl extends jBase {

	// ACL_DATA
	
	public static final String ACLs = "acls";
	
	
	public static final String ACL_DATA = "acldata";
	
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
	
	// ACL_ADMIN_TYPE
	
	public static final String ACL_ADMINS = "acladmins";
	
	@JsonIgnore
	private List<enAclAdmin> aclAdmins = new ArrayList<>();
	
	@JsonIgnore
	public final List<enAclAdmin> getAclAdmin() {
		return this.aclAdmins;
	}

	@JsonIgnore
	public final void setAclAdmin(List<enAclAdmin> value) {
		aclAdmins = (value == null ? new ArrayList<>() : value);
	}

	@JsonProperty(ACL_ADMINS)
	public final List<String> _getAclAdmin() {
		return this.aclAdmins.stream().map(enAbstract::name).collect(Collectors.toList());
	}
	
	@JsonProperty(ACL_ADMINS)
	public final void _setAclAdmin(List<String> value) {
		aclAdmins = value == null ? new ArrayList<>() : value.stream().map(enAclAdmin::valueOf).collect(Collectors.toList());
	}
	
	// OWNER
	
	public static final String OWNER = "owner";
	
	@JsonIgnore
	private enAclOwner owner = enAclOwner.OWNER_FALSE;
	
	@JsonIgnore
	public final enAclOwner getOwner() {
		return this.owner;
	}

	@JsonIgnore
	public final void setOwner(enAclOwner value) {
		owner = value == null ? enAclOwner.OWNER_FALSE : value;
	}

	@JsonProperty(OWNER)
	public final Boolean _getAclOwner() {
		return this.owner.value();
	}
	
	@JsonProperty(OWNER)
	public final void _setAclOwner(Boolean value) {
		this.owner = value == null ? enAclOwner.OWNER_FALSE : value == Boolean.FALSE ? enAclOwner.OWNER_FALSE : enAclOwner.OWNER_TRUE;
	}


	// CHILD_OBJECTS

	public final static String CHILD_OBJECTS = "childobjects";
	
	@JsonIgnore
	private List<jBaseLight> childObjects = new ArrayList<>();
	
	@JsonProperty(CHILD_OBJECTS)
	public List<jBaseLight> getChildObjects() {
		return childObjects;
	}

	@JsonProperty(CHILD_OBJECTS)
	public void setChildObjects(List<jBaseLight> value) {
		this.childObjects = value == null ? new ArrayList<>() : value;
	}

}
