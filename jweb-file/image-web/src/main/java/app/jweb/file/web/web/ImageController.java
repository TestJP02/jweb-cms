package app.jweb.file.web.web;

import app.jweb.file.api.FileRepository;
import app.jweb.file.web.service.ImageScalar;
import app.jweb.file.web.service.ImageSize;
import app.jweb.resource.Resource;
import app.jweb.util.MediaTypes;
import app.jweb.web.WebCache;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
@Path("/image")
public class ImageController {
    private static final Pattern IMAGE_PATTERN = Pattern.compile("image/([^/]*)/file/(.*)");

    @Inject
    ImageScalar imageScalar;

    @Inject
    @Named("image")
    WebCache cache;

    @Inject
    FileRepository fileRepository;

    @Inject
    UriInfo uriInfo;

    @Path("/{s:.+}")
    @GET
    public Response image() {
        String requestPath = uriInfo.getPath();

        Matcher matcher = IMAGE_PATTERN.matcher(requestPath);
        if (!matcher.matches()) {
            throw new NotFoundException(requestPath);
        }
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(Integer.MAX_VALUE);
        String filePath = matcher.group(2);
        Optional<Resource> resource = cache.get(requestPath);
        if (resource.isPresent()) {
            return Response.ok(resource.get()).cacheControl(cacheControl).build();
        }
        Resource file = fileRepository.get(filePath).orElseThrow(() -> new NotFoundException("missing file, requestPath=" + filePath));
        java.nio.file.Path targetPath = cache.path().resolve(requestPath);
        imageScalar.scale(file, new ImageSize(matcher.group(1)), targetPath);
        resource = cache.get(requestPath);
        if (resource.isPresent()) {
            return Response.ok(resource.get()).type(MediaTypes.getMediaType(filePath)).cacheControl(cacheControl).build();
        }
        throw new NotFoundException("missing image, requestPath=" + requestPath);
    }
}
