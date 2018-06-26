package io.sited.web.impl.controller;

import io.sited.resource.Resource;
import io.sited.util.MediaTypes;
import io.sited.web.WebRoot;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/static/{s:.+}")
public class StaticResourceController {
    @Inject
    WebRoot webRoot;

    @GET
    public Response get(@Context UriInfo uriInfo) {
        String path = uriInfo.getPath();
        Optional<Resource> resource = webRoot.get(path);
        if (!resource.isPresent()) {
            return Response.ok().status(Response.Status.NOT_FOUND).build();
        }
        Response.ResponseBuilder builder = Response.ok(resource.get()).type(MediaTypes.getMediaType(path));
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(Integer.MIN_VALUE);
        builder.cacheControl(cacheControl);
        return builder.build();
    }
}
