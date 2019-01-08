package app.jweb.web;

/**
 * @author chi
 */
public class NotAuthorizedWebException extends WebException {
    public NotAuthorizedWebException(String message, Object... args) {
        super(message, args);
    }
}
