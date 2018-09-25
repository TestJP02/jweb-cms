package app.jweb.util.exception;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author chi
 */
public interface Exceptions {
    static String stackTrace(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    static BadRequestException badRequestException(String field, String message) {
        BadRequestExceptionResponse response = new BadRequestExceptionResponse();
        response.field = field;
        response.message = message;
        return new BadRequestException(message, Response.status(400).entity(response).build());
    }

    static ExceptionResponse response(Throwable e) {
        ExceptionResponse response = new ExceptionResponse();
        response.message = e.getMessage();
        return response;
    }

    static BadRequestExceptionResponse response(BadRequestException e) {
        Response response = e.getResponse();
        if (response != null) {
            Object entity = response.getEntity();
            if (entity instanceof BadRequestExceptionResponse) {
                return (BadRequestExceptionResponse) entity;
            }
        }
        BadRequestExceptionResponse badRequestExceptionResponse = new BadRequestExceptionResponse();
        badRequestExceptionResponse.message = e.getMessage();
        return badRequestExceptionResponse;
    }
}
