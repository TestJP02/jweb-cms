package io.sited.web;

import io.sited.ApplicationException;

/**
 * @author chi
 */
public class WebApplicationException extends ApplicationException {
    public WebApplicationException(String message, Object... args) {
        super(message, args);
    }
}
