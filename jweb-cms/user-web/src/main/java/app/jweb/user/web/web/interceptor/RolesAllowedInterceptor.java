package app.jweb.user.web.web.interceptor;

import javax.annotation.Priority;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.ForbiddenException;
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
@Priority(Priorities.AUTHORIZATION)
public class RolesAllowedInterceptor implements ContainerRequestFilter {
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
                throw new ForbiddenException("invalid permission");
            }
        }
    }
}
