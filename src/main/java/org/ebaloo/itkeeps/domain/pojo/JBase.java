package org.ebaloo.itkeeps.domain.pojo;

import java.util.Optional;

import org.ebaloo.itkeeps.domain.Guid;
import org.ebaloo.itkeeps.domain.vertex.Base;
import org.ebaloo.itkeeps.domain.vertex.BaseStandard;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JBase {
	
	public JBase() {
		
	}
	
	public JBase(Base base) {
		
		this.setGuid(base.getGuid().toString());
		this.setName(base.getName());
		this.setType(base.getType());
		this.setEnable(base.isEnable());
		this.setCreationDate(base.getCreationDate());
		this.setDescription(base.getDescription());
		
	}
	
	public void update(Base base) {
		
		if(this.isPresentName())
			base.setName(this.getName());

		if(this.isPresentDescription())
			base.setDescription(this.getDescription());

	}



	// GUID
	
	@JsonIgnore
	private Optional<String> guid = Optional.empty();
	
	@JsonProperty(Base.GUID)
	public final String getGuid() {
		return this.guid.orElse(null);
	}

	@JsonProperty(Base.GUID)
	public final void setGuid(String guid) {
		this.guid = Optional.of(guid == null ? "" : guid);
	}

	@JsonIgnore
	public final void setGuid(Guid guid) {
		this.guid = Optional.of(guid == null ? "" : guid.toString());
	}
	
	
	@JsonIgnore
	public final boolean isPresentGuid() {
		return this.guid.isPresent();
	}

	
	// NAME
	
	@JsonIgnore
	private Optional<String> name = Optional.empty();
	
	@JsonProperty(Base.NAME)
	public final String getName() {
		return name.orElse(null);
	}

	@JsonProperty(Base.NAME)
	public final void setName(String name) {
		this.name = Optional.of(name == null ? "" : name);
	}

	@JsonIgnore
	public final boolean isPresentName() {
		return this.name.isPresent();
	}
	
	
	// TYPE
	
	@JsonIgnore
	private Optional<String> type = Optional.empty();
	
	@JsonProperty(Base.CLASS_TYPE)
	public final String getType() {
		return this.type.orElse(null);
	}

	@JsonProperty(Base.CLASS_TYPE)
	public final void setType(String value) {
		this.type = Optional.of(value == null ? "" : value);
	}

	@JsonIgnore
	public final boolean isPresentType() {
		return this.type.isPresent();
	}

	
	// ENABLE
	
	@JsonIgnore
	private Optional<Boolean> enable = Optional.empty();
	
	@JsonProperty(Base.ENABLE)
	public final Boolean getEnable() {
		return this.enable.orElse(null);
	}

	@JsonProperty(Base.ENABLE)
	public final void setEnable(Boolean value) {
		
		if(value == null)
			this.enable = Optional.empty();
		else
			this.enable = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isPresentEnable() {
		return this.enable.isPresent();
	}

	
	// CREATION_DATE
	
	@JsonIgnore
	private Optional<DateTime> creationDate = Optional.empty();
	
	@JsonProperty(Base.CREATION_DATE)
	public final DateTime getCreationDate() {
		return this.creationDate.orElse(null);
	}

	@JsonProperty(Base.CREATION_DATE)
	public final void setCreationDate(DateTime value) {
		
		if(value == null)
			this.creationDate = Optional.empty();
		else
			this.creationDate = Optional.of(value);
	}

	@JsonIgnore
	public final boolean isPresentCreationDate() {
		return this.creationDate.isPresent();
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
