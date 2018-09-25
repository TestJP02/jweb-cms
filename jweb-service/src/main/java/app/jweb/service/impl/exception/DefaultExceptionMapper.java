package app.jweb.service.impl.exception;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Priority(Priorities.USER)
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        ExceptionResponse response = new ExceptionResponse();
        response.errorMessage = e.getMessage();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response)
            .type(MediaType.APPLICATION_JSON).build();
    }
}