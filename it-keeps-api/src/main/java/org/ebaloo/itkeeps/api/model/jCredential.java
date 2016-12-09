package org.ebaloo.itkeeps.api.model;


import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.enumeration.enAuthentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("ALL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class jCredential extends jBaseStandard {

	
	// ID

	public static final String ID = "id";

	@JsonProperty(ID)
	private String id = null;

	public String getId() {
		return id;
	}

	@JsonIgnore
	public void setId(String id) {
		this.id = id;
	}

	// PASSWORD 
	
	public static final String PASSWORD64 = "password64";

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<String> password64 = Optional.empty();

	@JsonProperty(PASSWORD64)
	public String getPassword64() {
		return password64.orElse(StringUtils.EMPTY);
	}

	@JsonProperty(PASSWORD64)
	public void setPassword64(String value) {
		this.password64 = Optional.ofNullable(value);
	}

	@JsonIgnore
	public boolean isPresentPassword64() {
		return this.password64.isPresent();
	}
	

	// USER_NAME

	public static final String USER_NAME = "userName";

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<String> userName = Optional.empty();
	
	@JsonProperty(USER_NAME)
	public final String getUserName() {
		return this.userName.orElse(null);
	}

	@JsonProperty(USER_NAME)
	public final void setUserName(String value) {
		this.userName = Optional.ofNullable(value);
	}

	@JsonIgnore
	public final boolean isPresentUserName() {
		return this.userName.isPresent();
	}

	// USER

	public static final String USER = "user";

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<jBaseLight> user = Optional.empty();
	
	@JsonProperty(USER)
	public final jBaseLight getUser() {
		return this.user.orElse(null);
	}

	@JsonProperty(USER)
	public final void setUser(jBaseLight value) {
		this.user = Optional.ofNullable(value);
	}

	@JsonIgnore
	public final boolean isPresentUser() {
		return this.user.isPresent();
	}
	
	// AUTHENTICATION_TYPE
	
	public static final String AUTHENTICATION_TYPE = "authenticationType";
	
	@JsonIgnore
	private enAuthentication authenticationType = enAuthentication.BASIC;
	
	@JsonIgnore
	public final enAuthentication getAuthenticationType() {
		return this.authenticationType;
	}

	@JsonIgnore
	public final void setAuthenticationType(enAuthentication value) {
		authenticationType = value;
	}

	@JsonProperty(AUTHENTICATION_TYPE)
	public final String _getAuthenticationType() {
		return this.authenticationType.name();
	}

	@JsonProperty(AUTHENTICATION_TYPE)
	public final void _setAuthenticationType(String name) {
		authenticationType = (enAuthentication) enAuthentication.valueOf(name);
	}
	


}
