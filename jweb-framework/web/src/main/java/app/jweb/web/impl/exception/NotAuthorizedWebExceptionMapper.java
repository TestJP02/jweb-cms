package app.jweb.web.impl.exception;

import app.jweb.web.NotAuthorizedWebException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;
import java.net.URI;

@Priority(Priorities.USER - 1000)
public class NotAuthorizedWebExceptionMapper extends AbstractWebApplicationExceptionMapper<NotAuthorizedWebException> {
    public NotAuthorizedWebExceptionMapper() {
        super(Response.Status.UNAUTHORIZED);
    }

    @Override
    public Response toResponse(NotAuthorizedWebException exception) {
        return Response.temporaryRedirect(URI.create("/user/login")).build();
    }
}