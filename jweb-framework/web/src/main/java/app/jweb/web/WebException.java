package app.jweb.web;

import app.jweb.ApplicationException;

/**
 * @author chi
 */
public class WebException extends ApplicationException {
    public final AppInfo app;
    public final RequestInfo request;
    public final ClientInfo client;

    public WebException(AppInfo app, RequestInfo request, ClientInfo client, String message, Object... args) {
        super(message, args);
        this.app = app;
        this.request = request;
        this.client = client;
    }
}
