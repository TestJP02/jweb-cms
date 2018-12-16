package app.jweb.user.web.exception;

import org.slf4j.helpers.MessageFormatter;

import javax.ws.rs.NotAuthorizedException;

/**
 * @author miller
 */
public class WebNotAuthorizedException extends NotAuthorizedException {
    private final String requestPath;

    public WebNotAuthorizedException(String requestPath, String message, Object... args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage());
        this.requestPath = requestPath;
    }

    public String requestPath() {
        return requestPath;
    }
}
