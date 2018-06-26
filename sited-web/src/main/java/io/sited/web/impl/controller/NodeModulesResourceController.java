package io.sited.web.impl.controller;

import io.sited.resource.Resource;
import io.sited.util.MediaTypes;
import io.sited.web.WebRoot;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/node_modules/{s:.+}")
public class NodeModulesResourceController {
    @Inject
    WebRoot webRoot;

    @GET
    public Response robots(@Context UriInfo uriInfo) {
        String path = uriInfo.getPath();
        Optional<Resource> resource = webRoot.get(path);
        if (!resource.isPresent()) {
            return Response.ok().status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(resource.get()).type(MediaTypes.getMediaType(path)).build();
    }
}
