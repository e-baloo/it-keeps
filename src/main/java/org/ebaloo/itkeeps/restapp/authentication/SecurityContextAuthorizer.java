package org.ebaloo.itkeeps.restapp.authentication;


import javax.security.auth.Subject;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.tools.SecurityRole;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityContextAuthorizer implements SecurityContext {

    private Principal principal;
    private javax.inject.Provider<UriInfo> uriInfo;
    private Set<String> roles = new HashSet<String>();

    public SecurityContextAuthorizer(javax.inject.Provider<UriInfo> uriInfo, String guid, List<String> roles) {

        if (StringUtils.isEmpty(guid)) {
            this.principal = new Principal() {
                @Override
                public String getName() {
                    return "anonymous";
                }

                @Override
                public boolean implies(Subject subject) {
                    return true;
                }
            };

            this.roles.add(SecurityRole.GUEST.toString());
            
            
        } else {
        	
            this.principal = new Principal() {
                @Override
                public String getName() {
                    return guid;
                }

                @Override
                public boolean implies(Subject subject) {
                    return true;
                }
            };

            if(roles != null)
            	this.roles = roles.stream().collect(Collectors.toSet()); 
        }
        
        
        this.uriInfo = uriInfo;
        
    }


	public Principal getUserPrincipal() {
        return this.principal;
    }

    public boolean isUserInRole(String role) {
        return this.roles.contains(((role == null) ? "" : role));
    }

    public boolean isSecure() {
        return "https".equals(uriInfo.get().getRequestUri().getScheme());
    }

    public String getAuthenticationScheme() {
        return SecurityContext.DIGEST_AUTH;
    }
}