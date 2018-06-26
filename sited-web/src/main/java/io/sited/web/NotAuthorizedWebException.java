package io.sited.web;

/**
 * @author chi
 */
public class NotAuthorizedWebException extends WebApplicationException {
    public NotAuthorizedWebException(String message, Object... args) {
        super(message, args);
    }
}
