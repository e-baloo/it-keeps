package org.ebaloo.itkeeps.api.model;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Guid;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class jBase extends jObject {

	public static final String PARENT = "parent";
	public static final String PARENTS = "parents";
	public static final String CHILDS = "childs";
	public static final String CHILD = "child";
	
	
	
	//----------------------------
	
	public static final String ENABLE = "enable";
	public static final String CREATION_DATE = "creationDate";
	public static final String DESCRIPTION = "description";
	public static final String NAME = jBaseLight.NAME;
	public static final String CLASS_TYPE = jBaseLight.CLASS_TYPE;
	public static final String GUID = jBaseLight.GUID;
	public static final String VERSION = jBaseLight.VERSION;
	
	
	public jBase() {
		
	}
	
	@JsonIgnore
	public final jBaseLight getJBaseLight() {
		
		jBaseLight j = new jBaseLight();
		
		if(this.isPresentGuid())
			j.setGuid(this.getGuid());
		
		if(this.isPresentName())
			j.setName(this.getName());

		if(this.isPresentJObject()) {
			
			if(this.getJObject().isPresentType())
				j.setType(this.getJObject().getType());
			
			if(this.getJObject().isPresentVersion())
				j.setVersion(this.getJObject().getVersion());
		}

		
		return j;
		
	}


	@JsonProperty("_object")
	private JObject jobject = null;
	
	public JObject getJObject() {
		
		if(jobject == null)
			jobject = new JObject();
		
		return jobject;
	}
	
	@JsonIgnore
	public boolean isPresentJObject() {
		return jobject != null;
	}


	// GUID
	
	@JsonIgnore
	private Optional<Guid> guid = Optional.empty();
	
	@JsonIgnore
	public final Guid getGuid() {
		return this.guid.orElse(null);
	}

	@JsonProperty(GUID)
	private final String _getGuid() {
		return guid.isPresent() ?  guid.get().toString() : null;
	}

	@JsonIgnore
	public final void setGuid(Guid guid) {
		this.guid = Optional.ofNullable(guid);
	}

	@JsonProperty(GUID)
	private final void _setGuid(String guid) {
		this.guid = StringUtils.isEmpty(guid) ? Optional.empty() : Optional.of(new Guid(guid));
	}

	@JsonIgnore
	public final boolean isPresentGuid() {
		return this.guid.isPresent();
	}

	
	// NAME
	
	@JsonIgnore
	private Optional<String> name = Optional.empty();
	
	@JsonProperty(NAME)
	public final String getName() {
		return name.orElse(null);
	}

	@JsonProperty(NAME)
	public final void setName(String name) {
		this.name = Optional.ofNullable(name);
	}

	@JsonIgnore
	public final boolean isPresentName() {
		return this.name.isPresent();
	}
	
	


	
	public static class JObject {
		
		public JObject() {
			
		}
		
		// TYPE
		
		@JsonIgnore
		private Optional<String> type = Optional.empty();
		
		@JsonProperty(CLASS_TYPE)
		public final String getType() {
			return this.type.orElse(null);
		}

		@JsonProperty(CLASS_TYPE)
		public final void setType(String value) {
			this.type = Optional.ofNullable(value);
		}

		@JsonIgnore
		public final boolean isPresentType() {
			return this.type.isPresent();
		}

		
		// VERSION
		
		
		@JsonIgnore
		private Optional<Integer> version = Optional.empty();
		
		@JsonProperty(VERSION)
		public final Integer getVersion() {
			return this.version.orElse(null);
		}

		@JsonProperty(VERSION)
		public final void setVersion(Integer value) {
			this.version = Optional.ofNullable(value);
		}

		@JsonIgnore
		public final boolean isPresentVersion() {
			return this.version.isPresent();
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
			this.enable = Optional.ofNullable(value);
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
			this.creationDate = Optional.ofNullable(value);
		}
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
		this.description = Optional.ofNullable(value);
	}

	@JsonIgnore
	public boolean isPresentDescription() {
		return this.description.isPresent();
	}

	
	


}