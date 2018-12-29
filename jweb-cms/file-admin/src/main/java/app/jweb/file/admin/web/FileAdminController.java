package app.jweb.file.admin.web;

import app.jweb.file.api.FileRepository;
import app.jweb.resource.Resource;
import app.jweb.util.MediaTypes;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/admin/file/upload")
public class FileAdminController {
    @Inject
    FileRepository fileRepository;
    @Inject
    UriInfo uriInfo;

    @Path("/{s:.+}")
    @GET
    public Response file() {
        String path = uriInfo.getPath().substring("/admin/file".length());
        Optional<Resource> resource = fileRepository.get(path);
        if (!resource.isPresent()) {
            return Response.ok().status(Response.Status.NOT_FOUND).build();
        }
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(Integer.MAX_VALUE);
        return Response.ok(resource.get()).type(MediaTypes.getMediaType(path)).cacheControl(cacheControl).build();
    }
}
