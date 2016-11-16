package org.ebaloo.itkeeps.restapp.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.ebaloo.itkeeps.restapp.auth.JwtFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger logger = LoggerFactory.getLogger(JwtFactory.class.getName());

	@Override
	public Response toResponse(Exception exception) {

		logger.error("toResponse() : ", exception);

		Response response;

		logger.info(exception.toString());

		if (exception instanceof WebApplicationException) {

			WebApplicationException webEx = (WebApplicationException) exception;
			response = new ExceptionResponse(webEx.getResponse().getStatus(), webEx.getMessage()).getResponse();

		} else {

			response = new ExceptionResponse(Response.Status.INTERNAL_SERVER_ERROR).getResponse();

		}
		return response;
	}

}