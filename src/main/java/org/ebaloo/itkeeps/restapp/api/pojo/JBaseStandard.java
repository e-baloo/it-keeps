package org.ebaloo.itkeeps.restapp.api.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JBaseStandard extends JBase{

	// ICON
	
	@JsonIgnore
	private Optional<String> icon = Optional.empty();
	
	@JsonProperty("icon")
	public String getIcon() {
		return icon.orElse(null);
	}

	@JsonProperty("icon")
	public void setIcon(String value) {
		this.icon = Optional.of(value == null ? "" : value);
	}

	@JsonIgnore
	public boolean isPresentIcon() {
		return this.icon.isPresent();
	}
	

	// OTHER NAME
	
	@JsonIgnore
	private Optional<List<String>> otherName = Optional.empty();
	
	@JsonProperty("otherName")
	public List<String> getOtherName() {
		return this.otherName.orElse(null);
	}

	@JsonProperty("otherName")
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
	
	@JsonProperty("externalRef")
	public Map<String, String> getExternalRef() {
		return this.externalRef.orElse(null);
	}

	@JsonProperty("externalRef")
	public void setExternalRef(Map<String, String> value) {
		this.externalRef = Optional.of(value == null ? new HashMap<String, String>() : value);
	}

	@JsonIgnore
	public boolean isPresentExternalRef() {
		return this.externalRef.isPresent();
	}
	
}
