package app.jweb.user.admin.service;

import app.jweb.user.admin.exception.AdminNotAuthorizedException;
import app.jweb.web.Cookies;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.net.URI;

/**
 * @author chi
 */
public class AdminNotAuthorizedExceptionHandler implements ExceptionMapper<AdminNotAuthorizedException> {
    @Override
    public Response toResponse(AdminNotAuthorizedException exception) {
        return Response
            .temporaryRedirect(URI.create("/admin/user/login"))
            .cookie(Cookies.newCookie("fromURL", exception.requestPath()))
            .build();
    }
}
