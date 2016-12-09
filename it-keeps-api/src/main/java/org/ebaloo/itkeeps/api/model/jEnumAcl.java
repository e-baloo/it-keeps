package org.ebaloo.itkeeps.api.model;

import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class jEnumAcl extends jObject {

	@JsonProperty("data")
	public final List<String> getAclData() {
		return enAclData.values().stream().map(enAbstract::name).collect(Collectors.toList());
	}
	
	@JsonProperty("admin")
	public final List<String> getAclAdmin() {
		return enAclAdmin.values().stream().map(enAbstract::name).collect(Collectors.toList());
	}
	
	@JsonProperty("owner")
	public final List<String> getAclOwner() {
		return enAclOwner.values().stream().map(enAbstract::name).collect(Collectors.toList());
	}
	
	@JsonProperty("role")
	public final List<String> getAclRole() {
		return enAclRole.values().stream().map(enAbstract::name).collect(Collectors.toList());
	}

}
