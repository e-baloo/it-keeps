package org.ebaloo.itkeeps.api.model;

import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.enAuthentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class jEnumAuthentication extends jObject {

	@JsonProperty("type")
	public final List<String> getAclData() {
		return enAuthentication.values().stream().map(e -> e.name()).collect(Collectors.toList());
	}

}
