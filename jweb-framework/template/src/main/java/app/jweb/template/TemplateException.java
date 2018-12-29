package app.jweb.template;


import app.jweb.ApplicationException;

/**
 * @author chi
 */
public class TemplateException extends ApplicationException {
    public TemplateException(String message, Object... args) {
        super(message, args);
    }
}
