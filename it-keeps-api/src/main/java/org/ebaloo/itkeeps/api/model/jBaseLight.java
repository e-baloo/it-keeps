package org.ebaloo.itkeeps.api.model;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Guid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class jBaseLight extends jObject {

	public static final String NAME = "name";
	public static final String CLASS_TYPE = "classType";
	public static final String GUID = "guid";
	public static final String VERSION = "version";
	
	public jBaseLight() {
		
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
		this.guid = Optional.of(guid);
	}

	@JsonProperty(GUID)
	private final void _setGuid(String guid) {
		this.guid = StringUtils.isEmpty(guid) ? Optional.of(null) : Optional.of(new Guid(guid));
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
		this.name = Optional.of(name == null ? "" : name);
	}

	@JsonIgnore
	public final boolean isPresentName() {
		return this.name.isPresent();
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
			this.type = Optional.of(value == null ? "" : value);
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
			
			if(value == null)
				this.version = Optional.empty();
			else
				this.version = Optional.of(value);
		}

		@JsonIgnore
		public final boolean isPresentVersion() {
			return this.version.isPresent();
		}
		
	

}