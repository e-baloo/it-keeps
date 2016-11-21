package org.ebaloo.itkeeps.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class jToken extends jObject  {
	
	
	public jToken() {
	};
	

	public jToken(final String token) {
		this.token = token;
	}

	
	public static final String TOKEN = "token";
	
	// TOKEN
	
	@JsonIgnore
	private String token = null;
	
	@JsonProperty(TOKEN)
	public final String getToken() {
		return token;
	}

	@JsonProperty(TOKEN)
	public final void setToken(String token) {
		this.token = token;
	}

	
}