package org.ebaloo.itkeeps.api.model;


import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.enumeration.enAuthenticationType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("ALL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class jCredential extends jBase {


    // CRED

    //public static final String CRED = "cred";
    public static final String PASSWORD64 = "password64";
    public static final String USER_NAME = "userName";
    public static final String USER = "user";


    // PASSWORD
    public static final String AUTHENTICATION_TYPE = "authenticationType";

    //@JsonProperty(CRED)
    //private String cred = null;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<String> password64 = Optional.empty();
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
    private Optional<String> userName = Optional.empty();
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
    private Optional<jBaseLight> user = Optional.empty();


    // USER_NAME
    @JsonIgnore
    private enAuthenticationType authenticationType = enAuthenticationType.BASIC;

    /*public String getCred() {
        return cred;
    }*/

    //@JsonIgnore
    //public void setCred(String id) {
    //    this.cred = id;
    //}

	@JsonProperty(PASSWORD64)
	public String getPassword64() {
		return password64.orElse(StringUtils.EMPTY);
	}

	@JsonProperty(PASSWORD64)
	public void setPassword64(String value) {
		this.password64 = Optional.ofNullable(value);
	}

    // USER

	@JsonIgnore
	public boolean isPresentPassword64() {
		return this.password64.isPresent();
	}

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

	@JsonProperty(USER)
	public final jBaseLight getUser() {
		return this.user.orElse(null);
	}

    // AUTHENTICATION_TYPE

    @JsonProperty(USER)
	public final void setUser(jBaseLight value) {
		this.user = Optional.ofNullable(value);
	}

    @JsonIgnore
	public final boolean isPresentUser() {
		return this.user.isPresent();
	}
	
	@JsonIgnore
	public final enAuthenticationType getAuthenticationType() {
		return this.authenticationType;
	}

	@JsonIgnore
	public final void setAuthenticationType(enAuthenticationType value) {
		authenticationType = value;
	}

	@JsonProperty(AUTHENTICATION_TYPE)
	public final String _getAuthenticationType() {
		return this.authenticationType.name();
	}

	@JsonProperty(AUTHENTICATION_TYPE)
	public final void _setAuthenticationType(String name) {
		authenticationType = (enAuthenticationType) enAuthenticationType.valueOf(name);
	}
	


}
