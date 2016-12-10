package org.ebaloo.itkeeps.api.model;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Rid;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings({"ALL", "OptionalUsedAsFieldOrParameterType"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class jBase extends jObject {

	public static final String PARENT = "parent";
	public static final String PARENTS = "parents";
	public static final String CHILDS = "childs";
	public static final String CHILD = "child";
	
	
	
	//----------------------------
	
	public static final String CREATION_DATE = "creationDate";
	public static final String DESCRIPTION = "description";
	public static final String NAME = jBaseLight.NAME;
	public static final String CLASS_TYPE = jBaseLight.CLASS_TYPE;
	public static final String RID = jBaseLight.RID;
	public static final String VERSION = jBaseLight.VERSION;
	public static final String _OBJECT = "_object";
	@JsonProperty(_OBJECT)
	private JObject jobject = null;
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@JsonIgnore
	private Optional<Rid> rid = Optional.empty();
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@JsonIgnore
	private Optional<String> name = Optional.empty();

	ressWarnings("OptionalUsedAsFieldOrParameterType")

	@JsonIgnore
	private Optional<String> description = Optional.empty();

	@J
	
	public jBase() {

	}


	// GUID
	
	@JsonIgnore
	public final jBaseLight getJBaseLight() {

		jBaseLight j = new jBaseLight();

		if(this.isPresentRid())
			j.setRid(this.getRid());

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
	
	public JObject getJObject() {

		if(jobject == null)
			jobject = new JObject();

		return jobject;
	}

	@JsonIgnore
	public boolean isPresentJObject() {
		return jobject != null;
	}

	@JsonIgnore
	public final Rid getRid() {
		return this.rid.orElse(null);
	}

	@JsonIgnore
	public final void setRid(Rid rid) {
		this.rid = Optional.ofNullable(rid);
	}

	@JsonProperty(RID)
	private String _getRid() {
		return rid.isPresent() ? rid.get().getSimple() : null;
	}


	// NAME
	
	@JsonProperty(RID)
	private void _setRid(String rid) {
		this.rid = StringUtils.isEmpty(rid) ? Optional.empty() : Optional.of(new Rid(rid));
	}

	@JsonIgnore
	public final boolean isPresentRid() {
		return this.rid.isPresent();
	}

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

	DESCRIPTION

			@Supp
	Property(DESCRIPTION)

	public String getDescription() {
		return this.description.orElse(null);
	}

	@Jsson
	Property(DESCRIPTION)

	public void setDescription(String value) {
		this.description = Optional.ofNullable(value);
	}

	@Json
	Ignore

	public boolean isPresentDescription() {
		return this.description.isPresent();
	}


	on
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class JObject {

		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		@JsonIgnore
		private Optional<String> type = Optional.empty();

		// TYPE
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		@JsonIgnore
		private Optional<Integer> version = Optional.empty();
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		@JsonIgnore
		private Optional<DateTime> creationDate = Optional.empty();

		public JObject() {

		}

		@JsonProperty(CLASS_TYPE)
		public final String getType() {
			return this.type.orElse(null);
		}


		// VERSION

		@JsonProperty(CLASS_TYPE)
		public final void setType(String value) {
			this.type = Optional.ofNullable(value);
		}

		@JsonIgnore
		public final boolean isPresentType() {
			return this.type.isPresent();
		}

		@JsonProperty(VERSION)
		public final Integer getVersion() {
			return this.version.orElse(null);
		}

		@JsonProperty(VERSION)
		public final void setVersion(Integer value) {
			this.version = Optional.ofNullable(value);
		}


		// CREATION_DATE

		@JsonIgnore
		public final boolean isPresentVersion() {
			return this.version.isPresent();
		}

		@JsonProperty(CREATION_DATE)
		public final DateTime getCreationDate() {
			return this.creationDate.orElse(null);
		}

		@JsonProperty(CREATION_DATE)
		public final void setCreationDate(DateTime value) {
			this.creationDate = Optional.ofNullable(value);
		}
	}


	//

}
