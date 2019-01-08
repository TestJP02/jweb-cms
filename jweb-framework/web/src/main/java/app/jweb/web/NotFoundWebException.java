package app.jweb.web;

/**
 * @author chi
 */
public class NotFoundWebException extends WebException {
    public NotFoundWebException(String message, Object... args) {
        super(message, args);
    }
}
