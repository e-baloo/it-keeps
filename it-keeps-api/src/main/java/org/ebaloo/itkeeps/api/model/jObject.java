package org.ebaloo.itkeeps.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class jObject {

	public static final ObjectMapper MAPPER = new ObjectMapper();

	static {
		MAPPER.registerModule(new JodaModule());
		MAPPER.configure(com.fasterxml.jackson.databind.SerializationFeature.
				WRITE_DATES_AS_TIMESTAMPS , false);
	}


	public String toString() {
		try {
			return MAPPER.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
