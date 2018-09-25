package app.jweb.web;

/**
 * @author chi
 */
public class InternalServerErrorWebException extends WebException {
    public InternalServerErrorWebException(AppInfo app, RequestInfo request, ClientInfo client, String message, Object... args) {
        super(app, request, client, message, args);
    }
}
