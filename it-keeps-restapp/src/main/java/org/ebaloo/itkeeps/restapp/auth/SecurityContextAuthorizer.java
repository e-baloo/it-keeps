package org.ebaloo.itkeeps.restapp.auth;


import javax.security.auth.Subject;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enSecurityRole;

import java.security.Principal;

public class SecurityContextAuthorizer implements SecurityContext {

    private Principal principal;
    private javax.inject.Provider<UriInfo> uriInfo;
    private enSecurityRole role = null;

    public SecurityContextAuthorizer(javax.inject.Provider<UriInfo> uriInfo, String guid, enSecurityRole role) {

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

            this.role = enSecurityRole.GUEST;
            
            
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

            if(role != null)
            	this.role = role; 
        }
        
        
        this.uriInfo = uriInfo;
        
    }


	public Principal getUserPrincipal() {
        return this.principal;
    }

	@Override
	public boolean isUserInRole(String role) {
		return isUserInRole(enSecurityRole.valueOf(role));
	}

    public boolean isUserInRole(enSecurityRole role) {
        return  this.role.isInRole(role);
    }

    public boolean isSecure() {
        return "https".equals(uriInfo.get().getRequestUri().getScheme());
    }

    public String getAuthenticationScheme() {
        return SecurityContext.DIGEST_AUTH;
    }


}