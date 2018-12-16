package app.jweb.user.admin.web.interceptor;

import app.jweb.user.admin.exception.AdminForbiddenException;

import javax.annotation.Priority;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

/**
 * @author chi
 */
@Priority(Priorities.AUTHORIZATION - 1000)
public class RolesAllowedAdminInterceptor implements ContainerRequestFilter {
    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        RolesAllowed rolesAllowed = resourceInfo.getResourceMethod().getDeclaredAnnotation(RolesAllowed.class);
        if (rolesAllowed == null || rolesAllowed.value().length == 0) {
            return;
        }
        SecurityContext securityContext = requestContext.getSecurityContext();
        for (String role : rolesAllowed.value()) {
            if (!securityContext.isUserInRole(role)) {
                throw new AdminForbiddenException("invalid permission");
            }
        }
    }
}
