package io.sited.web;

/**
 * @author chi
 */
public class InternalServerErrorWebException extends WebApplicationException {
    public InternalServerErrorWebException(String message, Object... args) {
        super(message, args);
    }
}
