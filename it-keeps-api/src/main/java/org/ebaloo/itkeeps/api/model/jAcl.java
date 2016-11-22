package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class jAcl extends jBase {

	// ACL_DATA_TYPE
	
	public static final String ACL_DATA = "aclData";
	
	@JsonIgnore
	private enAclData aclData = enAclData.DENY;
	
	@JsonIgnore
	public final enAclData getAclData() {
		return this.aclData;
	}

	@JsonIgnore
	public final void setAclData(enAclData value) {
		aclData = value == null ? enAclData.DENY : value;
	}

	@JsonProperty(ACL_DATA)
	public final String _getAclData() {
		return this.aclData.name();
	}
	
	@JsonProperty(ACL_DATA)
	public final void _setAclData(String value) {
		this.aclData = value == null ? enAclData.DENY : enAclData.valueOf(value);
	}
	
	// OWNER
	
	public static final String OWNER = "owner";
	
	@JsonIgnore
	private Boolean owner = false;
	
	@JsonProperty(OWNER)
	public final Boolean getOwner() {
		return this.owner;
	}

	@JsonProperty(OWNER)
	public final void setOwner(Boolean value) {
		owner = value == null ? false : value;
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
		aclAdmin = value == null ? new ArrayList<enAclAdmin>() : value;
	}

	@JsonProperty(ACL_ADMIN)
	public final List<String> _getAclAdmin() {
		return this.aclAdmin.stream().map(e -> e.name()).collect(Collectors.toList());
	}
	
	@JsonProperty(ACL_ADMIN)
	public final void _setAclAdmin(List<String> value) {
		aclAdmin = value == null ? new ArrayList<enAclAdmin>() : value.stream().map(e -> enAclAdmin.valueOf(e)).collect(Collectors.toList());
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
