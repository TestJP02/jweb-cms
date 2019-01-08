package app.jweb.web;

/**
 * @author chi
 */
public class ForbiddenWebException extends WebException {
    public ForbiddenWebException(String message, Object... args) {
        super(message, args);
    }
}
