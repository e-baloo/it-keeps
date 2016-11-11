package org.ebaloo.itkeeps.domain.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ebaloo.itkeeps.domain.vertex.BaseStandard;
import org.ebaloo.itkeeps.domain.vertex.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JUser extends JBaseStandard{

	
	public JUser() {
		super();
	}
	
	public JUser(User obj) {
		super(obj);
		
		this.setId(obj.getId());
		this.setId(obj.getId());
		
	}
	
	
	public void update(User obj) {
		super.update(obj);
		
		if(this.isPresentId())
			obj.setIcon(this.getIcon());

		if(this.isPresentRoles())
			obj.setRoles(this.getRoles());

	}
	
	// ID
	
	@JsonIgnore
	private Optional<String> id = Optional.empty();
	
	@JsonProperty(User.ID)
	public String getId() {
		return id.orElse(null);
	}

	@JsonProperty(User.ID)
	public void setId(String value) {
		this.id = Optional.of(value == null ? "" : value);
	}

	@JsonIgnore
	public boolean isPresentId() {
		return this.id.isPresent();
	}
	

	// OTHER NAME
	
	@JsonIgnore
	private Optional<List<String>> otherName = Optional.empty();
	
	@JsonProperty(BaseStandard.OTHER_NAME)
	public List<String> getOtherName() {
		return this.otherName.orElse(null);
	}

	@JsonProperty(BaseStandard.OTHER_NAME)
	public void setOtherName(List<String> value) {
		this.otherName = Optional.of(value == null ? new ArrayList<String>() : value);
	}

	@JsonIgnore
	public boolean isPresentOtherName() {
		return this.otherName.isPresent();
	}

	
	// TYPE
	
	@JsonIgnore
	private Optional<Map<String, String>> externalRef = Optional.empty();
	
	@JsonProperty(BaseStandard.EXTERNAL_REF)
	public Map<String, String> getExternalRef() {
		return this.externalRef.orElse(null);
	}

	@JsonProperty(BaseStandard.EXTERNAL_REF)
	public void setExternalRef(Map<String, String> value) {
		this.externalRef = Optional.of(value == null ? new HashMap<String, String>() : value);
	}

	@JsonIgnore
	public boolean isPresentExternalRef() {
		return this.externalRef.isPresent();
	}

	// DESCRIPTION
	
	@JsonIgnore
	private Optional<String> description = Optional.empty();
	
	@JsonProperty(BaseStandard.DESCRIPTION)
	public String getDescription() {
		return this.description.orElse(null);
	}

	@JsonProperty(BaseStandard.DESCRIPTION)
	public void setDescription(String value) {
		this.description = Optional.of(value == null ? "" : value);
	}

	@JsonIgnore
	public boolean isPresentDescription() {
		return this.description.isPresent();
	}

	// ROLES

	public static final String ROLES = "roles";

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
