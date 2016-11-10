package org.ebaloo.itkeeps.domain.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ebaloo.itkeeps.domain.vertex.BaseStandard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JBaseStandard extends JBase{

	
	public JBaseStandard() {
		super();
	}
	
	public JBaseStandard(BaseStandard obj) {
		super(obj);
		
		this.setIcon(obj.getIcon());
		this.setOtherName(obj.getOtherName());
		this.setExternalRef(obj.getExternalRef());
		this.setDescription(obj.getDescription());
		
	}
	
	
	public void update(BaseStandard obj) {
		super.update(obj);
		
		if(this.isPresentIcon())
			obj.setIcon(this.getIcon());

		if(this.isPresentOtherName())
			obj.putExternalRef(this.getExternalRef());

		if(this.isPresentOtherName())
			obj.putOtherName(this.getOtherName());

		if(this.isPresentDescription())
			obj.setDescription(this.getDescription());

	}
	
	// ICON
	
	@JsonIgnore
	private Optional<String> icon = Optional.empty();
	
	@JsonProperty(BaseStandard.ICON)
	public String getIcon() {
		return icon.orElse(null);
	}

	@JsonProperty(BaseStandard.ICON)
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

}
