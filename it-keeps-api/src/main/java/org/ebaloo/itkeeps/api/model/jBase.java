package org.ebaloo.itkeeps.api.model;

import java.util.Optional;

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
    public static final String RID = jBaseLight.ID;
    public static final String VERSION = jBaseLight.VERSION;
    public static final String LIGHT = "light";
    @JsonProperty(LIGHT)
    private jBaseLight light = new jBaseLight();
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@JsonIgnore
    private Optional<DateTime> creationDate = Optional.empty();
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@JsonIgnore
	private Optional<String> description = Optional.empty();


	/*
    public static final String _OBJECT = "_object";

	@JsonProperty(_OBJECT)
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
*/


	/*

	public static final String LIGHT = "light";

	@JsonProperty(LIGHT)
	private jBaseLight light = new jBaseLight() ;

	public jBaseLight getJbl() {
		return light;
	}


*/


    // GUID

    //@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    //@JsonIgnore
    //private Optional<Rid> rid = Optional.empty();

    public jBase() {

    }

//	@JsonProperty(ID)
//	private String _getRid() {
//		return rid.isPresent() ?  rid.get().getSimple() : null;
//	}

    @JsonIgnore
    public final jBaseLight getLight() {
        return this.light;
    }

//	@JsonProperty(ID)
//	private void _setRid(String rid) {
//		this.rid = StringUtils.isEmpty(rid) ? Optional.empty() : Optional.of(new Rid(rid));
//	}

	@JsonIgnore
	public final Rid getRid() {
        return this.getLight().getId();
    }


    // NAME

//	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
//	@JsonIgnore
//	private Optional<String> name = Optional.empty();

	@JsonIgnore
	public final void setRid(Rid rid) {
        this.getLight().setId(rid);
    }

	@JsonIgnore
	public final boolean isPresentRid() {
        return this.getLight().isPresentRid();
    }

    @JsonIgnore
    public final String getName() {
        return this.getLight().getName();
    }


//	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
//	@JsonIgnore
//	private Optional<String> type = Optional.empty();

    @JsonIgnore
    public final void setName(String name) {
        this.getLight().setName(name);
    }

	@JsonIgnore
	public final boolean isPresentName() {
        return this.getLight().isPresentName();
    }

    //	@JsonProperty(CLASS_TYPE)
    @JsonIgnore
    public final String getType() {
        return this.getLight().getType();
    }


    // VERSION


//	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
//	@JsonIgnore
//	private Optional<Integer> version = Optional.empty();

    //	@JsonProperty(CLASS_TYPE)
    @JsonIgnore
    public final void setType(String value) {
        this.getLight().setType(value);
    }

    @JsonIgnore
    public final boolean isPresentType() {
        return this.getLight().isPresentType();
    }

    @JsonIgnore
    public final Integer getVersion() {
        return this.getLight().getVersion();
    }


    // CREATION_DATE

    @JsonIgnore
    public final void setVersion(Integer value) {
        this.getLight().setVersion(value);
    }

    @JsonIgnore
    public final boolean isPresentVersion() {
        return this.getLight().isPresentVersion();
    }

    @JsonProperty(CREATION_DATE)
    public final DateTime getCreationDate() {
        return this.creationDate.orElse(null);
    }

	/*

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class JObject {

		public JObject() {

		}

		// TYPE


	}
*/

    // DESCRIPTION

    @JsonProperty(CREATION_DATE)
    public final void setCreationDate(DateTime value) {
        this.creationDate = Optional.ofNullable(value);
    }

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
