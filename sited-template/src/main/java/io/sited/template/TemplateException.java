package io.sited.template;


import io.sited.ApplicationException;

/**
 * @author chi
 */
public class TemplateException extends ApplicationException {
    public TemplateException(String message, Object... args) {
        super(message, args);
    }
}
