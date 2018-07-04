package io.sited.web;

/**
 * @author chi
 */
public class ForbiddenWebException extends WebException {
    public ForbiddenWebException(AppInfo app, RequestInfo request, ClientInfo client, String message, Object... args) {
        super(app, request, client, message, args);
    }
}
