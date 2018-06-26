package io.sited.web;

/**
 * @author chi
 */
public class NotFoundWebException extends WebApplicationException {
    public NotFoundWebException(String message, Object... args) {
        super(message, args);
    }
}
