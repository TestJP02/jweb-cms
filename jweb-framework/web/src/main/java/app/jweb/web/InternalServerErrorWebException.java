package app.jweb.web;

/**
 * @author chi
 */
public class InternalServerErrorWebException extends WebException {
    public InternalServerErrorWebException(String message, Object... args) {
        super(message, args);
    }
}
