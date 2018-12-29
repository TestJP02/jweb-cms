package app.jweb.template;

import app.jweb.ApplicationException;

/**
 * @author chi
 */
public class TemplateResourceException extends ApplicationException {
    public TemplateResourceException(String message, Object... args) {
        super(message, args);
    }
}
