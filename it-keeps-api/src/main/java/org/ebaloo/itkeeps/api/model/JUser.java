package org.ebaloo.itkeeps.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JUser extends JBaseStandard{

	public static final String USER_ID = "userId";
	public static final String PASSWORD = "password";
	public static final String ROLE = "role";
	public static final String IN_GROUP = "inGroup";

	
	public JUser() {
		super();
	}
	
	
	// ID
	
	@JsonIgnore
	private Optional<String> userId = Optional.empty();
	
	@JsonProperty(USER_ID)
	public String getUserId() {
		return userId.orElse(null);
	}

	@JsonProperty(USER_ID)
	public void setUserId(String value) {
		this.userId = Optional.of(value == null ? "" : value);
	}

	@JsonIgnore
	public boolean isPresentUserId() {
		return this.userId.isPresent();
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

	// PASSWORD
	
	@JsonIgnore
	private Optional<String> password = Optional.empty();
	
	@JsonProperty(PASSWORD)
	public final String getPassword() {
		return this.password.orElse(null);
	}

	@JsonProperty(PASSWORD)
	public final void setPassword(String value) {
		password = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isPresentPassword() {
		return this.password.isPresent();
	}

	// CHILD_GROUP
	
	@JsonIgnore
	private Optional<List<JBaseLight>> inGroup = Optional.empty();
	
	@JsonProperty(IN_GROUP)
	public final List<JBaseLight> getInGroup() {
		return this.inGroup.orElse(new ArrayList<JBaseLight>());
	}

	@JsonProperty(IN_GROUP)
	public final void setInGroup(List<JBaseLight> value) {
		if(value == null)
			inGroup = Optional.of(new ArrayList<JBaseLight>());
		else
			inGroup = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isChildGroup() {
		return this.inGroup.isPresent();
	}
}
