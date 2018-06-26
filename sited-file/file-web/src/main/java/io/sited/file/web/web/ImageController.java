package io.sited.file.web.web;

import io.sited.file.api.FileRepository;
import io.sited.file.web.service.CachedImageResourceRepository;
import io.sited.file.web.service.ImageScalar;
import io.sited.file.web.service.ImageSize;
import io.sited.resource.ByteArrayResource;
import io.sited.resource.Resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
@Path("/image")
public class ImageController {
    private static final Pattern IMAGE_PATTERN = Pattern.compile("/image/([^/]*)/(.*)");

    @Inject
    ImageScalar imageScalar;

    @Inject
    FileRepository fileRepository;

    @Inject
    CachedImageResourceRepository repository;
    @Inject
    UriInfo uriInfo;

    @Path("/{s:.+}")
    @GET
    public Response image() throws IOException {
        String path = uriInfo.getPath();
        Matcher matcher = IMAGE_PATTERN.matcher(path);
        if (!matcher.matches()) {
            throw new NotFoundException(path);
        }
        String imagePath = path.substring("/image/".length());
        Resource file = fileRepository.get(matcher.group(2)).orElseThrow(() -> new NotFoundException("missing file, path=" + matcher.group(2)));
        byte[] scaled = imageScalar.scale(file, new ImageSize(matcher.group(1)));
        ByteArrayResource image = new ByteArrayResource(imagePath, scaled, file.lastModified());
        repository.create(image);
        return Response.ok(image).build();
    }
}
