package app.jweb.user.admin.exception;


import org.slf4j.helpers.MessageFormatter;

import javax.ws.rs.NotAuthorizedException;

/**
 * @author chi
 */
public class AdminNotAuthorizedException extends NotAuthorizedException {
    private final String requestPath;

    public AdminNotAuthorizedException(String requestPath, String message, Object... args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage());
        this.requestPath = requestPath;
    }

    public String requestPath() {
        return requestPath;
    }
}
