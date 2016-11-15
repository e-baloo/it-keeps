package org.ebaloo.itkeeps.httpclient;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JCredential;
import org.ebaloo.itkeeps.api.model.JGroup;
import org.ebaloo.itkeeps.api.model.JUser;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class Main {

	public static Logger LOGGER = null;

	public static final ObjectMapper MAPPER = new ObjectMapper();

	static {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		LOGGER = LoggerFactory.getLogger("Main");
		
		
		MAPPER.registerModule(new JodaModule());
		MAPPER.configure(com.fasterxml.jackson.databind.SerializationFeature.
	    	    WRITE_DATES_AS_TIMESTAMPS , false);
	}

	public static final String URL = "http://localhost:8080";

	public static void main(String[] args) throws JsonProcessingException {
		// TODO Auto-generated method stub

		LOGGER.info("START");

		
		JCredential cred = new JCredential();
		cred.setId("marc");
		cred.setPassword("test45");

		JsonNode node = callJsonCreat("/auth/login", cred);
		LOGGER.info(MAPPER.writeValueAsString(node));
		
		token = node.get("token").asText();
		httpJsonClient = null;

		
		
		JUser neuwjuser = new JUser();
		Guid nguid = new Guid();
		neuwjuser.setUserId("ID : " + nguid.toString());
		neuwjuser.setPassword("password");
		neuwjuser.setName("NAME : " + nguid.toString());	
		JUser testUser =  MAPPER.treeToValue(callJsonCreat("/api/user", neuwjuser), JUser.class);
		
		
		JGroup jg_r = new JGroup();
		jg_r.setName("root");
		JGroup jg_n1 = new JGroup();
		JGroup jg_n2 = new JGroup();
		jg_n1.setName("node 1");
		jg_n2.setName("node 2");
		
		jg_r = MAPPER.treeToValue(callJsonCreat("/api/group", jg_r), JGroup.class);
		jg_n1 = MAPPER.treeToValue(callJsonCreat("/api/group", jg_n1), JGroup.class);
		jg_n2 = MAPPER.treeToValue(callJsonCreat("/api/group", jg_n2), JGroup.class);
		
		jg_r.getChildGroups().add(jg_n1.getJBaseLight());
		jg_r.getChildGroups().add(jg_n2.getJBaseLight());

		long tStart = System.currentTimeMillis();
		
		jg_r = MAPPER.treeToValue(callJsonUpdate("/api/group", jg_r), JGroup.class);
		
		long elapsedSeconds = (System.currentTimeMillis() - tStart);
		LOGGER.info(String.format("Query executed in %d ms", elapsedSeconds));
		

		testUser.getInGroups().add(jg_n1.getJBaseLight());
		testUser.getInGroups().add(jg_n2.getJBaseLight());
		testUser.getInGroups().add(jg_r.getJBaseLight());
		JsonNode user = callJsonUpdate("/api/user", testUser);
		LOGGER.info(MAPPER.writeValueAsString(user));
		
		user = callJsonRead("/api/user/" + testUser.getGuid());
		
		LOGGER.info(MAPPER.writeValueAsString(user));

		
		
		
		LOGGER.info("END");

	}

	private static HttpClient httpJsonClient = null;
	
	private static String token = null;

	private static HttpClient getHttpJsonClient() {

		if (httpJsonClient == null) {
			HttpClientBuilder builder = HttpClientBuilder.create();

			ArrayList<Header> headersList = new ArrayList<Header>();
			headersList.add(new BasicHeader("accept", "application/json"));
			headersList.add(new BasicHeader("content-type", "application/json"));

			if(token != null) {
				headersList.add(new BasicHeader("Authorization", "Bearer" + " "+ token));
			}
			
			builder.setDefaultHeaders(headersList);

			httpJsonClient = builder.build();
		}

		return httpJsonClient;
	}

	public static final String urlFormat(String url) {

		if (!StringUtils.startsWithIgnoreCase(url, URL)) {
			if (!StringUtils.startsWithIgnoreCase(url, "/")) {
				url = "/" + url;
			}
			url = URL + url;
		}

		return url;

	}

	public enum CRUD {
		CREATE, READ, UPDATE, PATCH, DELETE 
	}

	public static final JsonNode callJsonCreat(String url, Object data) {
		return callJsonCRUD(CRUD.CREATE, url, data);
	}

	public static final JsonNode callJsonRead(String url) {
		return callJsonCRUD(CRUD.READ, url, null);
	}

	public static final JsonNode callJsonUpdate(String url, Object data) {
		return callJsonCRUD(CRUD.UPDATE, url, data);
	}


	public static final JsonNode callJsonCRUD(CRUD command, String url, Object data) {

		long tStart = System.currentTimeMillis();
		
		url = urlFormat(url);

		HttpRequestBase request = null;

		switch (command) {
		case CREATE:
			request = new HttpPost(url);
			break;
		case DELETE:
			request = new HttpDelete(url);
			break;
		case READ:
			request = new HttpGet(url);
			break;
		case UPDATE:
			request = new HttpPut(url);
			break;
		case PATCH:
			request = new HttpPatch(url);
			break;
		default:
			throw new RuntimeException("unknow command [" + command + "]");

		}

		try {
			
			///////////////////////////////////////
			
			if(data != null) {

				switch (command) {
				case CREATE:
				case UPDATE:
				case PATCH:
						StringEntity stringEntity = new StringEntity(MAPPER.writeValueAsString(data));
						((HttpEntityEnclosingRequestBase) request).setEntity(stringEntity);
					break;

				default:
						throw new RuntimeException("data not null for command [" + command + "]");
				}
				
			}
			
			
			HttpResponse response = getHttpJsonClient().execute(request);

			// Check for HTTP response code: 200 = success
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			JsonNode node = MAPPER.readValue(response.getEntity().getContent(), JsonNode.class);
			
			long elapsedSeconds = (System.currentTimeMillis() - tStart);
			
			//if(LOGGER.isTraceEnabled())
				LOGGER.info(String.format("Query executed in %d ms", elapsedSeconds));
			
			return node;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
