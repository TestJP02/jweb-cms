package app.jweb.file.web.service;

import app.jweb.ApplicationException;

/**
 * @author chi
 */
public class ImageException extends ApplicationException {
    public ImageException(Throwable e) {
        super(e);
    }

    public ImageException(String message, Object... args) {
        super(message, args);
    }
}
