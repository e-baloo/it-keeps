package org.ebaloo.itkeeps.domain.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JCredentials {

	@JsonProperty("username")
	private String username = null;

	@JsonProperty("password")
	private String password = null;

	public String getUsername() {
		return username;
	}

	@JsonIgnore
	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonIgnore
	public void setPassword(String password) {
		this.password = password;
	}


	
}
