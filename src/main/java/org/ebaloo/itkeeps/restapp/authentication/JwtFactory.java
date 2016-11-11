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
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public final class JwtFactory {

	private static final Logger logger = LoggerFactory.getLogger(JwtFactory.class.getName());

	private static final Key KEY = MacProvider.generateKey();
	
	private static final BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	private static final String DEFAULT_PASSWORD = "it-k33p5!@2017";

	
	private static final String USER_ID = "user.id";
	private static final String USER_NAME = "user.name";
	private static final String USER_ROLES = "user.roles";
	
	
	static {
		JwtFactory.setPassword(DEFAULT_PASSWORD);
	}
	
	private static int timeout = 10; // 10 minutes

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(int expiryDelay) {
		
		if(expiryDelay < 1)
			expiryDelay = 1;
		
		if(expiryDelay > 120)
			expiryDelay = 120;
		
		JwtFactory.timeout = expiryDelay;
	}

	public static String getJwtString(final User user) {

		if (logger.isTraceEnabled())
			logger.trace("getJwtString()");
		
		
		Date expires = getExpiryDate();

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		JwtBuilder jwtBuilder = Jwts.builder();

		jwtBuilder.setHeaderParam("typ", "JWT");
		
		jwtBuilder.setIssuer("IT-Keeps");
		
		Map<String, Object> claimsProprties = new HashMap<String, Object>();
		
		claimsProprties.put(Base.GUID, JwtFactory.encrypt(user.getGuid().toString()));
		claimsProprties.put(USER_ID, JwtFactory.encrypt(user.getId()));
		claimsProprties.put(USER_NAME, JwtFactory.encrypt(user.getName()));
		claimsProprties.put(USER_ROLES, JwtFactory.encrypt(StringUtils.join(user.getRoles(), ",")));
		
		jwtBuilder.setClaims(claimsProprties);
		
		jwtBuilder.setId(JwtFactory.encrypt(UUID.randomUUID().toString()));


		jwtBuilder.setExpiration(expires);
		jwtBuilder.setIssuedAt(new Date());
		
		
		jwtBuilder.signWith(signatureAlgorithm, KEY);


		return jwtBuilder.compact();
	}

	public static final void isValid(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("isValid()");

		try {

			Jwts.parser().setSigningKey(KEY).parseClaimsJws(token.trim());

		} catch (Exception e) {
			
			throw new RuntimeException(e);
			
		}
	}

	public static final String getUserName(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("getJwtString()");

		Jws<Claims> claimsJws = getClaims(token);
		
		return JwtFactory.decrypt(claimsJws.getBody().get(USER_NAME, String.class));
	}

	public static final List<String> getRoles(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("getRoles()");

		Jws<Claims> claimsJws = getClaims(token);

		String userRoles = claimsJws.getBody().get(USER_ROLES, String.class);

		ArrayList<String> list = new ArrayList<String>();

		if (StringUtils.isNotEmpty(userRoles))
			list.addAll(Arrays.asList(JwtFactory.decrypt(userRoles).split(",")));

		return list;
	}

	public static final String getUserId(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("getVersion()");

		Jws<Claims> claimsJws = getClaims(token);

		return JwtFactory.decrypt(claimsJws.getBody().get(USER_ID, String.class));
	}

	public static final String getGuid(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("getGuid()");

		Jws<Claims> claimsJws = getClaims(token);

		return JwtFactory.decrypt(claimsJws.getBody().get(Base.GUID, String.class));
	}

	
	private static final Jws<Claims> getClaims(final String token) {
		
		if (logger.isTraceEnabled())
			logger.trace("getClaims()");

		isValid(token);
		
		return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
	}
	
	
	private static final Date getExpiryDate() {

		if (logger.isTraceEnabled())
			logger.trace("getExpiryDate()");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, timeout);
		
		return calendar.getTime();
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
