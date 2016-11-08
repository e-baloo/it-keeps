package org.ebaloo.itkeeps.restapp.authentication;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

	private static final Key key = MacProvider.generateKey();
	
	private static int expiryDelay = 10; // 10 minutes

	public static int getExpiryDelay() {
		return expiryDelay;
	}

	public static void setExpiryDelay(int expiryDelay) {
		
		if(expiryDelay < 1)
			expiryDelay = 1;
		
		if(expiryDelay > 120)
			expiryDelay = 120;
		
		JwtFactory.expiryDelay = expiryDelay;
	}

	public static String getJwtString(final String userId) {
		return getJwtString(userId, userId);
	}

	public static String getJwtString(final String userId, final String userName) {
		return getJwtString(userId, userName, null);
	}

	public static String getJwtString(final String userId, final String userName, final List<String> roles) {

		if (logger.isTraceEnabled())
			logger.trace("getJwtString()");
		
		
		if (StringUtils.isEmpty(userId))
			throw new NullPointerException("null or empty 'userId' is illegal");

		if (StringUtils.isEmpty(userName))
			throw new NullPointerException("null or empty 'userName' is illegal");

		Date expires = getExpiryDate();

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		JwtBuilder jwtBuilder = Jwts.builder();

		jwtBuilder.setIssuer("IT-Keeps");
		jwtBuilder.setId(userId);
		jwtBuilder.setSubject(userName);

		if (roles != null && roles.size() > 0)
			jwtBuilder.setAudience(StringUtils.join(roles, ","));

		jwtBuilder.setExpiration(expires);
		jwtBuilder.setIssuedAt(new Date());
		jwtBuilder.signWith(signatureAlgorithm, key);


		return jwtBuilder.compact();

	}

	public static final void isValid(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("isValid()");

		try {

			Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());

		} catch (Exception e) {
			
			throw new RuntimeException(e);
			
		}
	}

	public static final String getUserName(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("getJwtString()");

		Jws<Claims> claimsJws = getClaims(token);
		
		return claimsJws.getBody().getSubject();
	}

	public static final List<String> getRoles(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("getRoles()");

		Jws<Claims> claimsJws = getClaims(token);

		String audience = claimsJws.getBody().getAudience();

		ArrayList<String> list = new ArrayList<String>();

		if (StringUtils.isNotEmpty(audience))
			list.addAll(Arrays.asList(audience.split(",")));

		return list;
	}

	public static final String getUserId(final String token) {

		if (logger.isTraceEnabled())
			logger.trace("getVersion()");

		Jws<Claims> claimsJws = getClaims(token);

		return claimsJws.getBody().getId();
	}

	
	private static final Jws<Claims> getClaims(final String token) {
		
		if (logger.isTraceEnabled())
			logger.trace("getClaims()");

		isValid(token);
		
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
	}
	
	
	private static final Date getExpiryDate() {

		if (logger.isTraceEnabled())
			logger.trace("getExpiryDate()");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, expiryDelay);
		
		return calendar.getTime();
	}

}
