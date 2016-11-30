package org.ebaloo.itkeeps.api.model;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class jEncryptedEntry extends jBase {
	
	public static final String DATA = "data";
	public static final String MEDIA_TYPE = "mediaType";
	
	public jEncryptedEntry() {
		super();
	};
	

	// MEDIA TYPE
	
	@JsonIgnore
	private String mediaType = "text/plain";
	
	@JsonProperty(MEDIA_TYPE)
	public final String getMediaType() {
		return this.mediaType;
	}

	@JsonProperty(MEDIA_TYPE)
	public final void setMediaType(String value) {
		
		if(StringUtils.isEmpty(value))
			value = StringUtils.EMPTY;
		
		this.mediaType = value;
	}

	// DATA
	
	@JsonIgnore
	private String data = StringUtils.EMPTY;
	
	@JsonProperty(DATA)
	public final String getData() {
		return data;
	}

	@JsonProperty(DATA)
	public final void setData(String value) {
		
		if(StringUtils.isEmpty(value))
			value = StringUtils.EMPTY;
		
		this.data = value;
	}

}