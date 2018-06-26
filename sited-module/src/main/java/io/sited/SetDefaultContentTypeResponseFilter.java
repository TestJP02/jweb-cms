package io.sited;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author chi
 */
public class SetDefaultContentTypeResponseFilter implements ContainerResponseFilter {
    @Inject
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Method resourceMethod = resourceInfo.getResourceMethod();
        if (resourceMethod == null) {
            return;
        }
        Produces produces = resourceMethod.getDeclaredAnnotation(Produces.class);
        if (produces == null && (responseContext.getMediaType() == null || responseContext.getMediaType().isCompatible(MediaType.APPLICATION_OCTET_STREAM_TYPE))) {
            String accept = requestContext.getHeaderString(HttpHeaders.ACCEPT);
            if (accept != null && accept.contains(MediaType.APPLICATION_JSON)) {
                responseContext.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            }
        }
    }
}
