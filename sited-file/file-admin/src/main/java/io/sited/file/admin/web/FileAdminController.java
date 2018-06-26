package io.sited.file.admin.web;

import io.sited.file.api.FileRepository;
import io.sited.resource.Resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author chi
 */
@Path("/admin/file/file")
public class FileAdminController {
    @Inject
    FileRepository fileRepository;
    @Inject
    UriInfo uriInfo;

    @Path("/{s:.+}")
    @GET
    public Response file() {
        String path = uriInfo.getPath().substring("/admin/file".length());
        Resource resource = fileRepository.get(path).orElseThrow(() -> new NotFoundException("missing file, path=" + path));
        return Response.ok(resource).build();
    }
}
