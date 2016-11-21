package org.ebaloo.itkeeps.api.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class jAcl extends jBase {

	// ACL_DATA_TYPE
	
	public static final String ACL_DATA_TYPE = "aclDataType";
	
	@JsonIgnore
	private Optional<enAclData> aclDataType = Optional.empty();
	
	@JsonIgnore
	public final enAclData getAclDataType() {
		return this.aclDataType.orElse(null);
	}

	@JsonIgnore
	public final void setAclDataType(enAclData value) {
		aclDataType = Optional.of(value);
	}

	@JsonProperty(ACL_DATA_TYPE)
	public final String _getAclDataType() {
		if(aclDataType.isPresent())
			return this.aclDataType.get().name();
		else
			return null;
	}
	
	@JsonProperty(ACL_DATA_TYPE)
	public final void _setAclDataType(String value) {
		if(StringUtils.isEmpty(value))
			this.aclDataType = Optional.empty();
		else 
			aclDataType = Optional.of((enAclData) enAclData.valueOf(value));
	}
	
	@JsonIgnore
	public boolean isPresentAclDataType() {
		return this.aclDataType.isPresent();
	}

	
	// OWNER
	
	public static final String OWNER = "owner";
	
	@JsonIgnore
	private Optional<Boolean> owner = Optional.empty();
	
	@JsonProperty(OWNER)
	public final Boolean getOwner() {
		return this.owner.orElse(null);
	}

	@JsonProperty(OWNER)
	public final void setAclDataType(Boolean value) {
		owner = Optional.of(value);
	}

	@JsonIgnore
	public boolean isPresentOwner() {
		return this.owner.isPresent();
	}
	
	// ACL_ADMIN_TYPE
	
	public static final String ACL_ADMIN_TYPE = "aclAdminType";
	
	@JsonIgnore
	private Optional<List<enAclAdmin>> aclAdminType = Optional.empty();
	
	@JsonIgnore
	public final List<enAclAdmin> getAclAdminType() {
		return this.aclAdminType.orElse(null);
	}

	@JsonIgnore
	public final void setAclAdminType(List<enAclAdmin> value) {
		aclAdminType = Optional.of(value);
	}

	@JsonProperty(ACL_ADMIN_TYPE)
	public final List<String> _getAclAdminType() {
		if(aclAdminType.isPresent())
			return this.aclAdminType.get().stream().map(e -> e.toString()).collect(Collectors.toList());
		else
			return null;
	}
	
	@JsonProperty(ACL_ADMIN_TYPE)
	public final void _setAclAdminType(List<String> value) {
		if(value.isEmpty())
			this.aclAdminType = Optional.empty();
		else 
			aclAdminType = Optional.of(value.stream().map(e-> (enAclAdmin) enAclAdmin.valueOf(e)).collect(Collectors.toList()));
	}
	
	@JsonIgnore
	public boolean isPresentAclAdminType() {
		return this.aclAdminType.isPresent();
	}
	
}
