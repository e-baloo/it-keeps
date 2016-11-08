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
	
	public static final Key key = MacProvider.generateKey();

	private static final Logger logger = LoggerFactory.getLogger(JwtFactory.class.getName());


	public static String getJwtString(final String issuer, List<String> roles, int version, Key key) {

    	if(logger.isTraceEnabled())
    		logger.trace("getJwtString()");

        if (issuer == null) {
            throw new NullPointerException("null username is illegal");
        }
        
        /*
        if (roles == null) {
            throw new NullPointerException("null roles are illegal");
        }
        */
        
        /*
        if (expires == null) {
            throw new NullPointerException("null expires is illegal");
        }
        */
        
        if (key == null) {
            throw new NullPointerException("null key is illegal");
        }
    	

		Date expires = getExpiryDate(60);
		
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        
        JwtBuilder jwtBuilder = Jwts.builder();
        
        		jwtBuilder.setIssuer("Jersey-Security-Basic");
        		jwtBuilder.setSubject(issuer);
        		
        		if(roles != null && roles.size() > 0)
        			jwtBuilder.setAudience(StringUtils.join(roles, ","));
        		
        		jwtBuilder.setExpiration(expires);
        		jwtBuilder.setIssuedAt(new Date());
        		jwtBuilder.setId(String.valueOf(version));
        		jwtBuilder.signWith(signatureAlgorithm, key);
        		
        String jwtString = jwtBuilder.compact();
        		
        return jwtString;
		
		
	}
	
    public static final void isValid(final String token, final Key key) {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("isValid()");

        try {
        	
            Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());
            
        } catch (Exception e) {
        	
        	logger.error("isValid()", e);
        	
        	throw new RuntimeException(e);
        }
    }

    public static String getName(String jwsToken, Key key) {

    	isValid(jwsToken, key);
        
    
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
        return claimsJws.getBody().getSubject();
    }

    public static List<String> getRoles(String jwsToken, Key key) {

        isValid(jwsToken, key);
        
    	ArrayList<String> list = new ArrayList<String>();
    	
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
        return Arrays.asList(claimsJws.getBody().getAudience().split(","));
//        return new ArrayList<String>();
    }

    public static int getVersion(String jwsToken, Key key) {

    	isValid(jwsToken, key);
        
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            return Integer.parseInt(claimsJws.getBody().getId());
//        return -1;
    }
	
    private static Date getExpiryDate(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

}
