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
import org.ebaloo.itkeeps.api.model.JObject;
import org.ebaloo.itkeeps.api.model.JCredential;
import org.ebaloo.itkeeps.api.model.JToken;
import org.ebaloo.itkeeps.commons.ConfigFactory;
import org.ebaloo.itkeeps.commons.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class ItkeepsHttpClient {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ItkeepsHttpClient.class.getName());

	
	public static final ObjectMapper MAPPER = new ObjectMapper();

	static {
		MAPPER.registerModule(new JodaModule());
		MAPPER.configure(com.fasterxml.jackson.databind.SerializationFeature.
	    	    WRITE_DATES_AS_TIMESTAMPS , false);
	}

	
	private static String apiUrl = null;
	
	private static final String getApiUrl() {
		
		if(apiUrl == null)
			apiUrl = ConfigFactory.getString("client.url");
		
		return apiUrl;

	}
	
	
	
	public ItkeepsHttpClient(JCredential jcredential) {
		
		JToken _token = callJsonCreat("/auth/login", jcredential, JToken.class);
		token = _token.getToken();
		httpJsonClient = null;
		
	}
	
	
	
	private HttpClient httpJsonClient = null;
	private String token = null;

	private HttpClient getHttpJsonClient() {

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

	public final String urlFormat(String url) {

		if (!StringUtils.startsWithIgnoreCase(url, getApiUrl())) {
			if (!StringUtils.startsWithIgnoreCase(url, "/")) {
				url = "/" + url;
			}
			url = getApiUrl() + url;
		}

		return url;

	}

	public enum CRUD {
		CREATE, READ, UPDATE, PATCH, DELETE 
	}

	public final <T extends JObject> T callJsonCreat(String url, Object data, Class<T> target) {
		return callJsonCRUD(CRUD.CREATE, url, data, target);
	}

	public final <T extends JObject> T callJsonRead(String url, Class<T> target) {
		return callJsonCRUD(CRUD.READ, url, null, target);
	}

	public final <T extends JObject> T callJsonUpdate(String url, Object data, Class<T> target) {
		return callJsonCRUD(CRUD.UPDATE, url, data, target);
	}


	public final <T extends JObject> T callJsonCRUD(CRUD command, String url, Object data, Class<T> target) {

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
			logger.debug(String.format("==> Query executed in %d ms", elapsedSeconds));
			
			return MAPPER.treeToValue(node, target);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
