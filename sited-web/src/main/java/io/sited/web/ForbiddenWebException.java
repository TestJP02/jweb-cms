package io.sited.web;

/**
 * @author chi
 */
public class ForbiddenWebException extends WebApplicationException {
    public ForbiddenWebException(String message, Object... args) {
        super(message, args);
    }
}
