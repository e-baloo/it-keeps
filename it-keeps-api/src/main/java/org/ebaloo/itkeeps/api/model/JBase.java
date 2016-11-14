package org.ebaloo.itkeeps.api.model;

import java.util.Optional;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JBase extends JBaseLight {

	public static final String ENABLE = "enable";
	public static final String CREATION_DATE = "creationDate";
	public static final String DESCRIPTION = "description";

	
	public JBase() {
		
	}
	
	@JsonIgnore
	public final JBaseLight getJBaseLight() {
		
		JBaseLight j = new JBaseLight();
		
		if(this.isPresentGuid())
			j.setGuid(this.getGuid());
		
		if(this.isPresentName())
			j.setName(this.getName());

		if(this.isPresentJObject()) {
			
			if(this.getJObject().isPresentType())
				this.getJObject().setType(this.getJObject().getType());
			
			if(this.getJObject().isPresentVersion())
				this.getJObject().setVersion(this.getJObject().getVersion());
		}

		
		return j;
		
	}




	// DESCRIPTION
	
	@JsonIgnore
	private Optional<String> description = Optional.empty();
	
	@JsonProperty(DESCRIPTION)
	public String getDescription() {
		return this.description.orElse(null);
	}

	@JsonProperty(DESCRIPTION)
	public void setDescription(String value) {
		this.description = Optional.of(value == null ? "" : value);
	}

	@JsonIgnore
	public boolean isPresentDescription() {
		return this.description.isPresent();
	}

	
		// ENABLE
		
		@JsonIgnore
		private Optional<Boolean> enable = Optional.empty();
		
		@JsonProperty(ENABLE)
		public final Boolean getEnable() {
			return this.enable.orElse(null);
		}

		@JsonProperty(ENABLE)
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
		
		@JsonProperty(CREATION_DATE)
		public final DateTime getCreationDate() {
			return this.creationDate.orElse(null);
		}

		@JsonProperty(CREATION_DATE)
		public final void setCreationDate(DateTime value) {
			
			if(value == null)
				this.creationDate = Optional.empty();
			else
				this.creationDate = Optional.of(value);
		}


}
