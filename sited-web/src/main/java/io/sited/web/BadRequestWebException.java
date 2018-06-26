package io.sited.web;

/**
 * @author chi
 */
public class BadRequestWebException extends WebApplicationException {
    public BadRequestWebException(String message, Object... args) {
        super(message, args);
    }
}
