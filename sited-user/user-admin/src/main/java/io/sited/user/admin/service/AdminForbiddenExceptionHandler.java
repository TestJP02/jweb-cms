package io.sited.user.admin.service;

import io.sited.user.admin.exception.AdminForbiddenException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.net.URI;

/**
 * @author chi
 */
public class AdminForbiddenExceptionHandler implements ExceptionMapper<AdminForbiddenException> {
    @Override
    public Response toResponse(AdminForbiddenException exception) {
        return Response
            .temporaryRedirect(URI.create("/admin/user/forbidden"))
            .build();
    }
}
