package org.ebaloo.itkeeps.restapp.auth;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.commons.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;


public final class JwtFactory {

	public static final String CONF_TOKEN_KEY = "token.key";
	public static final String CONF_TOKEN_TIMEOUT = "token.timeout";
	private static final Logger logger = LoggerFactory.getLogger(JwtFactory.class.getName());
	private static final String DEFAULT_KEY = "tpONIG5BlO4wTRHFD8ehIvwbAdUMPAVmXNdPU6qYcVSb8zL85xkgmDr7sHHzNBIHpWI3l4a1rXPhnUs4lt57bjIVwHUW3Ot7gyfN6zoh6iJm83eCMkbXF73K7qRIv801";
	private static final String GENERAT_KEY = "{random}";
	private static final String USER_NAME = "user.name";
	private static final String USER_ROLE = "user.role";
	private static String __key = null;
	private static long DEFAULT_TIMEOUT = 10;
	private static Long timeout = null;

	private static String getKey() {

		if(__key == null) {

			String key = DEFAULT_KEY;

			if(ConfigFactory.hasPath(CONF_TOKEN_KEY)) {

				key = ConfigFactory.getString(CONF_TOKEN_KEY);

				if(StringUtils.isEmpty(key) || StringUtils.equals(key, GENERAT_KEY)) {

					logger.warn("Generate Password !!!");

					String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
					key = RandomStringUtils.random( 128, 0, 0, false, false, characters.toCharArray(), new SecureRandom() );

				}


			}

			__key = key;
		}


		return __key;
	}

	public static long getTimeout() {
		
		
		if(timeout == null) {
			timeout = DEFAULT_TIMEOUT;
		}
		
		
		 if(timeout < 1L)
			timeout = 1L;
		
		if(timeout > 60L)
			timeout = 60L;
		
		
		return timeout;
	}

	public static String getJwtString(final jUser user) {

		if (logger.isTraceEnabled())
			logger.trace("getJwtString()");
		
		final JWTSigner signer = new JWTSigner(getKey());

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim 
		final long exp = iat + 60L * getTimeout(); // expires claim. In this case the token expires in 60 seconds

		final HashMap<String, Object> claims = new HashMap<>();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("iss", "IT-Keeps");


		claims.put(jBase.RID, user.getId().getSimple());
		claims.put(jBase.NAME, user.getName());
		
		//claims.put(USER_ROLE, user.getRole().toString());
		claims.put(USER_ROLE, enRole.ROOT.toString());
		
		
		return signer.sign(claims);
	}

	public static Map<String, Object> isValid(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("isValid()");

		try {

			final JWTVerifier verifier = new JWTVerifier(getKey());
		    return verifier.verify(token);
		    
		} catch (Exception e) {
			
			throw new RuntimeException(e);
			
		}
	}

	public static String getUserName(Map<String, Object> claims) {

		if (logger.isTraceEnabled())
			logger.trace("getJwtString()");

		return claims.get(USER_NAME).toString();
	}

	/*
	public static final List<String> getRoles(Map<String, Object> claims) {

		if (logger.isTraceEnabled())
			logger.trace("getRoles()");


		String userRoles = claims.get(USER_ROLE).toString();

		ArrayList<String> list = new ArrayList<String>();

		if (StringUtils.isNotEmpty(userRoles))
			list.addAll(Arrays.asList(userRoles.split(",")));

		return list;
	}
	*/

	/*
	public static final String getUserId(Map<String, Object> claims) {

		if (logger.isTraceEnabled())
			logger.trace("getVersion()");

		return claims.get(USER_ID).toString();
	}
	*/

	public static String getRid(Map<String, Object> claims) {

		if (logger.isTraceEnabled())
			logger.trace("getId()");


		return claims.get(jBase.RID).toString(); 
	}

	public static enRole getRole(Map<String, Object> claims) {
		
		String role = claims.get(USER_ROLE).toString();

		return enRole.valueOf(role);
	}

	
/*	
	private static final BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	private static final String DEFAULT_PASSWORD = "it-k33p5!@2017";
	static {
		JwtFactory.setPassword(DEFAULT_PASSWORD);
	}

	public static void setPassword(String password) {
		JwtFactory.textEncryptor.setPassword(password);
	}
	
	private static final String encrypt(final String message) {
		return JwtFactory.textEncryptor.encrypt(message);
	}

	private static final String decrypt(final String encryptedMessage) {
		return JwtFactory.textEncryptor.decrypt(encryptedMessage);
	}
*/

}
