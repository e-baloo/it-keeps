package org.ebaloo.itkeeps.restapp.tools;

import static org.junit.Assert.*;

import java.time.Instant;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PingTest {

	@Test
	public void testGetPing() {

		//ObjectMapper mapper = (new JacksonJsonProvider()).getContext(null);

		Response response = (new PingEndpoint()).getPing();
		
		assertEquals("", response.getStatus(), Response.Status.OK.getStatusCode());

		System.out.println(response);
		System.out.println(response.getEntity());
		System.out.println(response.getMediaType());
		
		//JsonNode jn = mapper.convertValue((new PingEndpoint()).getPing().getEntity(), JsonNode.class);
		Instant now = Instant.now();
/*
		assertTrue("Faile node 'result' not exist", jn.has("result"));

		assertEquals("Faile not 'pong'", jn.get("result").asText(), "pong");

		assertTrue("Faile node 'now' not exist", jn.has("now"));

		assertTrue("Faile 'now' and now() is >100ms", now.toEpochMilli() <= Instant.parse(jn.get("now").asText()).toEpochMilli() + 100L);
*/
	}

}
