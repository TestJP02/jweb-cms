package io.sited.file.web.web;

import io.sited.file.api.FileRepository;
import io.sited.file.web.service.ImageResourceRepository;
import io.sited.file.web.service.ImageScalar;
import io.sited.file.web.service.ImageSize;
import io.sited.resource.ByteArrayResource;
import io.sited.resource.Resource;

import javax.inject.Inject;
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
    FileRepository fileRepository;

    @Inject
    ImageResourceRepository repository;
    @Inject
    UriInfo uriInfo;

    @Path("/{s:.+}")
    @GET
    public Response image() throws IOException {
        String path = uriInfo.getPath();
        Optional<Resource> resource = repository.get(path);
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(Integer.MAX_VALUE);
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
        repository.create(image);
        return Response.ok(image).cacheControl(cacheControl).build();
    }
}
