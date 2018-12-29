package app.jweb;

import app.jweb.util.exception.Exceptions;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author chi
 */
@Priority(Priorities.USER)
class DefaultExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public final Response toResponse(Throwable exception) {
        return Response.serverError().entity(Exceptions.stackTrace(exception)).type(MediaType.TEXT_PLAIN).build();
    }
}
