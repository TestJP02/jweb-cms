package io.sited.web;

/**
 * @author chi
 */
public class BadRequestWebException extends WebException {
    public BadRequestWebException(AppInfo app, RequestInfo request, ClientInfo client, String message, Object... args) {
        super(app, request, client, message, args);
    }
}
