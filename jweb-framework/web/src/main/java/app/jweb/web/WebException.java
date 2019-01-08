package app.jweb.web;

import app.jweb.ApplicationException;

/**
 * @author chi
 */
public class WebException extends ApplicationException {
    public WebException(String message, Object... args) {
        super(message, args);
    }
}
