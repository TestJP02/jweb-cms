package app.jweb.web.impl.exception;

import app.jweb.web.NotFoundWebException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;

@Priority(Priorities.USER - 1000)
public class NotFoundWebExceptionMapper extends AbstractWebApplicationExceptionMapper<NotFoundWebException> {
    public NotFoundWebExceptionMapper() {
        super(Response.Status.NOT_FOUND);
    }
}