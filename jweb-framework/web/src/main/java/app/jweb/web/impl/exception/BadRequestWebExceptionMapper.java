package app.jweb.web.impl.exception;

import app.jweb.web.BadRequestWebException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;

@Priority(Priorities.USER - 1000)
public class BadRequestWebExceptionMapper extends AbstractWebApplicationExceptionMapper<BadRequestWebException> {
    public BadRequestWebExceptionMapper() {
        super(Response.Status.BAD_REQUEST);
    }
}