package app.jweb.web;

/**
 * @author chi
 */
public class NotAuthorizedWebException extends WebException {
    public NotAuthorizedWebException(AppInfo app, RequestInfo request, ClientInfo client, String message, Object... args) {
        super(app, request, client, message, args);
    }
}
