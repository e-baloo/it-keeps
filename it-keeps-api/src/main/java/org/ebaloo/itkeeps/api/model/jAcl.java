package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.enumeration.enAclOwner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class jAcl extends jBase {

	// ACL_DATA
	
	public static final String ACL_DATA = "aclData";
	
	@JsonIgnore
	private List<enAclData> aclData = new ArrayList<enAclData>();
	
	@JsonIgnore
	public final List<enAclData> getAclData() {
		return this.aclData;
	}

	@JsonIgnore
	public final void setAclData(List<enAclData> value) {
		aclData = value == null ? new ArrayList<enAclData>() : value;
	}

	@JsonProperty(ACL_DATA)
	public final List<String> _getAclData() {
		return this.aclData.stream().map(e -> e.name()).collect(Collectors.toList());
	}
	
	@JsonProperty(ACL_DATA)
	public final void _setAclData(List<String> value) {
		this.aclData = value == null ? new ArrayList<enAclData>() : value.stream().map(e -> enAclData.valueOf(e)).collect(Collectors.toList());
	}
	
	// ACL_ADMIN_TYPE
	
	public static final String ACL_ADMIN = "aclAdmin";
	
	@JsonIgnore
	private List<enAclAdmin> aclAdmin = new ArrayList<enAclAdmin>();
	
	@JsonIgnore
	public final List<enAclAdmin> getAclAdmin() {
		return this.aclAdmin;
	}

	@JsonIgnore
	public final void setAclAdmin(List<enAclAdmin> value) {
		aclAdmin = (value == null ? new ArrayList<enAclAdmin>() : value);
	}

	@JsonProperty(ACL_ADMIN)
	public final List<String> _getAclAdmin() {
		return this.aclAdmin.stream().map(e -> e.name()).collect(Collectors.toList());
	}
	
	@JsonProperty(ACL_ADMIN)
	public final void _setAclAdmin(List<String> value) {
		aclAdmin = value == null ? new ArrayList<enAclAdmin>() : value.stream().map(e -> enAclAdmin.valueOf(e)).collect(Collectors.toList());
	}
	
	// OWNER
	
	public static final String OWNER = "owner";
	
	@JsonIgnore
	private enAclOwner owner = enAclOwner.FALSE;
	
	@JsonIgnore
	public final enAclOwner getOwner() {
		return this.owner;
	}

	@JsonIgnore
	public final void setOwner(enAclOwner value) {
		owner = value == null ? enAclOwner.FALSE : value;
	}

	@JsonProperty(OWNER)
	public final Boolean _getAclOwner() {
		return this.owner.value();
	}
	
	@JsonProperty(OWNER)
	public final void _setAclOwner(Boolean value) {
		this.owner = value == null ? enAclOwner.FALSE : value == Boolean.FALSE ? enAclOwner.FALSE : enAclOwner.TRUE;
	}


	// CHILD_OBJECTS

	public final static String CHILD_OBJECTS = "childObjects";
	
	@JsonIgnore
	private List<jBaseLight> childObjects = new ArrayList<jBaseLight>();
	
	@JsonProperty(CHILD_OBJECTS)
	public List<jBaseLight> getChildObjects() {
		return childObjects;
	}

	@JsonProperty(CHILD_OBJECTS)
	public void setChildObjects(List<jBaseLight> value) {
		this.childObjects = value == null ? new ArrayList<jBaseLight>() : value;
	}

}
