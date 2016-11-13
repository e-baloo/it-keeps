package org.ebaloo.itkeeps.api.model;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JUser extends JBaseStandard{

	public static final String ID = "id";
	public static final String ROLE = "role";

	
	public JUser() {
		super();
	}
	
	
	// ID
	
	@JsonIgnore
	private Optional<String> id = Optional.empty();
	
	@JsonProperty(ID)
	public String getId() {
		return id.orElse(null);
	}

	@JsonProperty(ID)
	public void setId(String value) {
		this.id = Optional.of(value == null ? "" : value);
	}

	@JsonIgnore
	public boolean isPresentId() {
		return this.id.isPresent();
	}
	
	
	// ROLE
	
	@JsonIgnore
	private Optional<String> role = Optional.empty();
	
	@JsonProperty(ROLE)
	public final String getRole() {
		return this.role.orElse(null);
	}

	@JsonProperty(ROLE)
	public final void setRole(String value) {
		role = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isPresentRole() {
		return this.role.isPresent();
	}

	
}
