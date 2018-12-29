package app.jweb.web.impl.exception;

import app.jweb.web.ForbiddenWebException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;

@Priority(Priorities.USER - 1000)
public class ForbiddenWebExceptionMapper extends AbstractWebApplicationExceptionMapper<ForbiddenWebException> {
    public ForbiddenWebExceptionMapper() {
        super(Response.Status.FORBIDDEN);
    }
}