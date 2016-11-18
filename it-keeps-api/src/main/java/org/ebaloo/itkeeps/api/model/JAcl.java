package org.ebaloo.itkeeps.api.model;

import org.ebaloo.itkeeps.api.AclType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JAcl extends JBase {

	// AUTHENTICATION_TYPE
	
	public static final String ACL_TYPE = "aclType";
	
	@JsonIgnore
	private AclType aclType = AclType.NONE;
	
	@JsonProperty(ACL_TYPE)
	public final AclType getAuthenticationType() {
		return this.aclType;
	}

	@JsonProperty(ACL_TYPE)
	public final void setAuthenticationType(AclType value) {
		aclType = value;
	}

	
	
	
}
