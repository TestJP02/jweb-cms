package app.jweb.user.admin.exception;


import javax.ws.rs.ForbiddenException;

/**
 * @author chi
 */
public class AdminForbiddenException extends ForbiddenException {
    public AdminForbiddenException(String message) {
        super(message);
    }
}
