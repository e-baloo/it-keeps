package org.ebaloo.itkeeps.api.model;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Rid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("ALL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class jBaseLight extends jObject {

	public static final String NAME = "name";
	public static final String CLASS_TYPE = "type";
    public static final String ID = Rid.RDI_NAME;
    public static final String VERSION = "ver";


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonIgnore
	private Optional<Rid> rid = Optional.empty();


	// GUID
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@JsonIgnore
	private Optional<String> name = Optional.empty();
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@JsonIgnore
	private Optional<String> type = Optional.empty();
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@JsonIgnore
	private Optional<Integer> version = Optional.empty();

	public jBaseLight() {

	}

	@JsonIgnore
    public final Rid getId() {
        return this.rid.orElse(null);
	}

	@JsonIgnore
    public final void setId(Rid id) {
        this.rid = Optional.of(id);
    }


    @JsonProperty(value = ID, index = 10)
    private String _getId() {
        return rid.isPresent() ? rid.get().getSimple() : null;
	}

    @JsonProperty(value = ID, index = 10)
    private void _setId(String guid) {
        this.rid = StringUtils.isEmpty(guid) ? Optional.of(null) : Optional.of(new Rid(guid));
	}

	@JsonIgnore
	public final boolean isPresentRid() {
		return this.rid.isPresent();
	}

    @JsonProperty(value = NAME, index = 100)
    public final String getName() {
		return name.orElse(null);
	}


    @JsonProperty(value = NAME, index = 100)
    public final void setName(String name) {
		this.name = Optional.of(name == null ? "" : name);
	}

	@JsonIgnore
	public final boolean isPresentName() {
		return this.name.isPresent();
	}

		@JsonProperty(CLASS_TYPE)
		public final String getType() {
			return this.type.orElse(null);
		}

		@JsonProperty(CLASS_TYPE)
		public final void setType(String value) {
			this.type = Optional.of(value == null ? "" : value);
		}


	// VERSION
		
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
			
			if(value == null)
				this.version = Optional.empty();
			else
				this.version = Optional.of(value);
		}

		@JsonIgnore
		public final boolean isPresentVersion() {
			return this.version.isPresent();
		}

		@Override
		public boolean equals(Object obj) {
			
			System.out.println("jBaseLight.equals");
			
			if(!(obj instanceof jBaseLight) && !(obj instanceof Rid))
				return false;
			
			if(!this.isPresentRid())
				return false;
			
			Rid _rid = null;
			
			if(obj instanceof jBaseLight) {
				jBaseLight j = (jBaseLight) obj;
				
				if(!j.isPresentRid())
					return false;

                _rid = j.getId();
            } else {
				_rid = (Rid) obj;
			}


            return this.getId().equals(_rid);

        }
		
	

}
