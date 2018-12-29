package app.jweb.web;

/**
 * @author chi
 */
public class NotFoundWebException extends WebException {
    public NotFoundWebException(AppInfo app, RequestInfo request, ClientInfo client, String message, Object... args) {
        super(app, request, client, message, args);
    }
}
