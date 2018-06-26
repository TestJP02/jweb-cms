package io.sited.web.impl.exception;

import io.sited.web.NotAuthorizedWebException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;

@Priority(Priorities.USER - 1000)
public class BadRequestWebExceptionMapper extends AbstractWebApplicationExceptionMapper<NotAuthorizedWebException> {
    public BadRequestWebExceptionMapper() {
        super(Response.Status.BAD_REQUEST);
    }
}