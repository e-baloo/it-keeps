package org.ebaloo.itkeeps.restapp.auth;


import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;

@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request,
            ContainerResponseContext response) {

        String origin = request.getHeaderString("origin");


        response.getHeaders().add("Access-Control-Allow-Origin", StringUtils.isEmpty(origin) ? "*" : origin);

        response.getHeaders().add("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");

        response.getHeaders().add("Access-Control-Allow-Credentials", "true");

        response.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}