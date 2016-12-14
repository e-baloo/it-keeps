package org.ebaloo.itkeeps.api.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonValue;
import org.ebaloo.itkeeps.api.enumeration.enAbstract;
import org.ebaloo.itkeeps.api.enumeration.enAuthenticationType;

public class jAuthenticationTypeEnum extends jObject {

	/*
	@JsonValue
	public final List<String> get() {
		return enAuthenticationType.values().stream().map(enAbstract::name).collect(Collectors.toList());
	}

*/
}
