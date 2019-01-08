package app.jweb.web;

/**
 * @author chi
 */
public class BadRequestWebException extends WebException {
    public BadRequestWebException(String message, Object... args) {
        super(message, args);
    }
}
