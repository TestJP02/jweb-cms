package app.jweb.web.impl.exception;

import app.jweb.web.WebException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;

@Priority(Priorities.USER - 1000)
public class DefaultWebExceptionMapper extends AbstractWebApplicationExceptionMapper<WebException> {
    public DefaultWebExceptionMapper() {
        super(Response.Status.INTERNAL_SERVER_ERROR);
    }
}