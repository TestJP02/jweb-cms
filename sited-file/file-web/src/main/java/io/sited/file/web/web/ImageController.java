package io.sited.file.web.web;

import io.sited.file.api.FileRepository;
import io.sited.file.web.service.ImageScalar;
import io.sited.file.web.service.ImageSize;
import io.sited.resource.ByteArrayResource;
import io.sited.resource.Resource;
import io.sited.web.WebCache;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
@Path("/image")
public class ImageController {
    private static final Pattern IMAGE_PATTERN = Pattern.compile("image/([^/]*)/(.*)");

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
    public Response image() throws IOException {
        String path = uriInfo.getPath();
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(Integer.MAX_VALUE);

        Optional<Resource> resource = cache.get(path);
        if (resource.isPresent()) {
            return Response.ok(resource.get()).cacheControl(cacheControl).build();
        }

        Matcher matcher = IMAGE_PATTERN.matcher(path);
        if (!matcher.matches()) {
            throw new NotFoundException(path);
        }
        Resource file = fileRepository.get(matcher.group(2)).orElseThrow(() -> new NotFoundException("missing file, path=" + matcher.group(2)));
        byte[] scaled = imageScalar.scale(file, new ImageSize(matcher.group(1)));
        ByteArrayResource image = new ByteArrayResource(path, scaled, file.lastModified());
        cache.create(image);
        return Response.ok(image).cacheControl(cacheControl).build();
    }
}
