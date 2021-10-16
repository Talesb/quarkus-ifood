package com.github.talesb.ifood.infra;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	public Response toResponse(ConstraintViolationException exception) {
		return Response.status(Status.BAD_REQUEST).entity(ConstraintViolationResponse.of(exception)).build();
	}

}
