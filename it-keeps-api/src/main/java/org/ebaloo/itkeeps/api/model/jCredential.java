package org.ebaloo.itkeeps.api.model;


import java.util.Optional;

import org.ebaloo.itkeeps.api.enumeration.enAuthentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class jCredential {

	
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
	
	public static final String PASSWORD = "password";

	@JsonIgnore
	private String password = null;

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty(PASSWORD)
	public void setPassword(String password) {
		this.password = password;
	}


	// USER_NAME

	public static final String USER_NAME = "userName";

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
