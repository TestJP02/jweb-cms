package app.jweb.user.web.service;

import app.jweb.user.web.web.ajax.UserAJAXController;
import app.jweb.user.web.exception.WebNotAuthorizedException;
import app.jweb.web.Cookies;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.net.URI;

/**
 * @author miller
 */
public class WebNotAuthorizedExceptionHandler implements ExceptionMapper<WebNotAuthorizedException> {
    @Override
    public Response toResponse(WebNotAuthorizedException exception) {
        String requestPath = exception.requestPath();
        return Response
            .temporaryRedirect(URI.create("/user/login"))
            .cookie(Cookies.newCookie(UserAJAXController.COOKIE_FROM_URL, requestPath.charAt(0) == '/' ? requestPath : "/" + requestPath))
            .build();
    }
}
