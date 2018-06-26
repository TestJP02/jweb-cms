package io.sited.admin.impl.web;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import io.sited.admin.ConsoleBundle;
import io.sited.admin.impl.service.Console;
import io.sited.resource.CombinedResource;
import io.sited.resource.Resource;
import io.sited.util.MediaTypes;
import io.sited.web.WebRoot;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author chia
 */
@Path("/admin/static")
public class AdminStaticResourceController {
    @Inject
    Console console;

    @Inject
    WebRoot webRoot;

    @Path("/{s:.+}")
    @GET
    @Consumes
    public Response get(@Context UriInfo uriInfo, @Context Request request) throws IOException {
        String path = uriInfo.getPath();
        Optional<ConsoleBundle> consoleBundleOptional = console.findByScriptFile(path);
        Resource resource;
        if (consoleBundleOptional.isPresent()) {
            ConsoleBundle consoleBundle = consoleBundleOptional.get();
            resource = new CombinedResource(path, resourcePaths(consoleBundle), webRoot);
        } else {
            Optional<Resource> optional = webRoot.get(path);
            if (!optional.isPresent()) {
                throw new NotFoundException(String.format("missing resource, path=%s", path));
            }
            resource = optional.get();
        }
        EntityTag eTag = new EntityTag(Integer.toString(resource.hashCode()));
        Response.ResponseBuilder builder = request.evaluatePreconditions(eTag);
        if (builder == null) {
            builder = Response.ok(resource).type(MediaTypes.getMediaType(Files.getFileExtension(path)));
            builder.tag(eTag);
        }
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMustRevalidate(true);
        builder.cacheControl(cacheControl);
        return builder.build();
    }

    private List<String> resourcePaths(ConsoleBundle consoleBundle) {
        List<String> resourcePaths = Lists.newArrayList();
        resourcePaths.add(consoleBundle.scriptFile);
        resourcePaths.addAll(consoleBundle.scriptFiles);
        return resourcePaths;
    }
}
