package org.ebaloo.itkeeps.restapp.authentication;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.domain.vertex.Base;
import org.ebaloo.itkeeps.domain.vertex.User;
import org.glassfish.jersey.internal.util.Base64;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;


public final class JwtFactory {

	private static final Logger logger = LoggerFactory.getLogger(JwtFactory.class.getName());

//	private static final Key KEY = MacProvider.generateKey();
	private static final String KEY = ("test");
	
	private static final BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	private static final String DEFAULT_PASSWORD = "it-k33p5!@2017";

	
	private static final String USER_ID = "user.id";
	private static final String USER_NAME = "user.name";
	private static final String USER_ROLES = "user.roles";
	
	
	static {
		JwtFactory.setPassword(DEFAULT_PASSWORD);
	}
	
	private static long timeout = 10; // 10 minutes

	public static long getTimeout() {
		return timeout;
	}

	public static void setTimeout(long timeout) {
		
		if(timeout < 1)
			timeout = 1;
		
		if(timeout > 120)
			timeout = 120;
		
		JwtFactory.timeout = timeout;
	}

	public static String getJwtString(final User user) {

		if (logger.isTraceEnabled())
			logger.trace("getJwtString()");
		
		

		
		/*
		 * 
		 * 
final String issuer = "https://mydomain.com/";
final String secret = "{{secret used for signing}}";


final JWTSigner signer = new JWTSigner(secret);
final HashMap<String, Object> claims = new HashMap<String, Object>();
claims.put("iss", issuer);

final String jwt = signer.sign(claims);
		 */
		
		//SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		//JwtBuilder jwtBuilder = Jwts.builder();

		final JWTSigner signer = new JWTSigner(KEY);

		

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim 
		final long exp = iat + 60L * getTimeout(); // expires claim. In this case the token expires in 60 seconds

		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("iss", "IT-Keeps");		
		
		
		claims.put(Base.GUID, user.getGuid().toString());
		claims.put(USER_ID, user.getId());
		claims.put(USER_NAME, user.getName());
		claims.put(USER_ROLES, StringUtils.join(user.getRoles(), ","));
		
		
		return signer.sign(claims);
	}

	public static final Map<String, Object> isValid(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("isValid()");

		try {

			final JWTVerifier verifier = new JWTVerifier(KEY);
		    return verifier.verify(token);
		    
		} catch (Exception e) {
			
			throw new RuntimeException(e);
			
		}
	}

	public static final String getUserName(Map<String, Object> claims) {

		if (logger.isTraceEnabled())
			logger.trace("getJwtString()");

		return claims.get(USER_NAME).toString();
	}

	public static final List<String> getRoles(Map<String, Object> claims) {

		if (logger.isTraceEnabled())
			logger.trace("getRoles()");


		String userRoles = claims.get(USER_ROLES).toString();

		ArrayList<String> list = new ArrayList<String>();

		if (StringUtils.isNotEmpty(userRoles))
			list.addAll(Arrays.asList(userRoles.split(",")));

		return list;
	}

	public static final String getUserId(Map<String, Object> claims) {

		if (logger.isTraceEnabled())
			logger.trace("getVersion()");

		return claims.get(USER_ID).toString();
	}

	public static final String getGuid(Map<String, Object> claims) {

		if (logger.isTraceEnabled())
			logger.trace("getGuid()");


		return claims.get(Base.GUID).toString(); 
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

	/*


textEncryptor.setPassword(myEncryptionPassword);
...
String myEncryptedText = textEncryptor.encrypt(myText);
...
String plainText = textEncryptor.decrypt(myEncryptedText);

*/

}
