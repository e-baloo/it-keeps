package org.ebaloo.itkeeps.api.model;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JUser extends JBaseStandard{

	public static final String ID = "id";
	public static final String ROLES = "roles";

	
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
	
	// ROLES

	@JsonIgnore
	private Optional<List<String>> roles = Optional.empty();
	
	@JsonProperty(ROLES)
	public final List<String> getRoles() {
		return this.roles.orElse(null);
	}

	@JsonProperty(ROLES)
	public final void setRoles(List<String> value) {
		roles = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isPresentRoles() {
		return this.roles.isPresent();
	}
}
