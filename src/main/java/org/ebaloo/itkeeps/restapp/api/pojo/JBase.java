package org.ebaloo.itkeeps.restapp.api.pojo;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JBase {

	// NAME
	
	@JsonIgnore
	private Optional<String> name = Optional.empty();
	
	@JsonProperty("name")
	public final String getName() {
		return name.orElse(null);
	}

	@JsonProperty("name")
	public final void setName(String name) {
		this.name = Optional.of(name == null ? "" : name);
	}

	@JsonIgnore
	public final boolean isPresentName() {
		return this.name.isPresent();
	}
	

	// GUID
	
	@JsonIgnore
	private Optional<String> guid = Optional.empty();
	
	@JsonProperty("guid")
	public final String getGuid() {
		return this.guid.orElse(null);
	}

	@JsonProperty("guid")
	public final void setGuid(String guid) {
		this.guid = Optional.of(guid == null ? "" : guid);
	}

	@JsonIgnore
	public final boolean isPresentGuid() {
		return this.guid.isPresent();
	}

	// TYPE
	
	@JsonIgnore
	private Optional<String> type = Optional.empty();
	
	@JsonProperty("type")
	public final String getType() {
		return this.type.orElse(null);
	}

	@JsonProperty("type")
	public final void setType(String guid) {
		this.type = Optional.of(guid == null ? "" : guid);
	}

	@JsonIgnore
	public final boolean isPresentType() {
		return this.type.isPresent();
	}
	
}
