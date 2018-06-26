package io.sited.user.admin.service;

import io.sited.user.admin.exception.AdminNotAuthorizedException;
import io.sited.web.Cookies;

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
