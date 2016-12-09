package org.ebaloo.itkeeps.api.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("ALL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class jBaseStandard extends jBase{

	public static final String EXTERNAL_REF = "externealRef";
	public static final String OTHER_NAME = "otherName";
	public static final String ICON = "icon";

	
	public jBaseStandard() {
		super();
	}
	

	
	// ICON
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<String> icon = Optional.empty();
	
	@JsonProperty(ICON)
	public String getIcon() {
		return icon.orElse(null);
	}

	@JsonProperty(ICON)
	public void setIcon(String value) {
		this.icon = Optional.ofNullable(value);
	}

	@JsonIgnore
	public boolean isPresentIcon() {
		return this.icon.isPresent();
	}
	

	// OTHER NAME
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<List<String>> otherName = Optional.empty();
	
	@JsonProperty(OTHER_NAME)
	public List<String> getOtherName() {
		return this.otherName.orElse(null);
	}

	@JsonProperty(OTHER_NAME)
	public void setOtherName(List<String> value) {
		this.otherName = Optional.ofNullable(value);
	}

	@JsonIgnore
	public boolean isPresentOtherName() {
		return this.otherName.isPresent();
	}

	
	// TYPE
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<Map<String, String>> externalRef = Optional.empty();
	
	@JsonProperty(EXTERNAL_REF)
	public Map<String, String> getExternalRef() {
		return this.externalRef.orElse(null);
	}

	@JsonProperty(EXTERNAL_REF)
	public void setExternalRef(Map<String, String> value) {
		this.externalRef = Optional.ofNullable(value);
	}

	@JsonIgnore
	public boolean isPresentExternalRef() {
		return this.externalRef.isPresent();
	}

}
