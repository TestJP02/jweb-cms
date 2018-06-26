package io.sited.file.web.web;


import com.google.common.collect.Maps;
import io.sited.file.api.FileRepository;
import io.sited.resource.Resource;
import io.sited.util.MediaTypes;
import io.sited.web.Template;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
@Path("/file")
public class FileController {
    @Inject
    FileRepository fileRepository;
    @Inject
    UriInfo uriInfo;

    @Path("/{s:.+}")
    @GET
    public Response file(@QueryParam("path") String path, @QueryParam("query") String query) {
        String uriPath = uriInfo.getPath();
        if (uriPath.endsWith("/")) {
            return directory(uriPath, path, query);
        }
        Resource resource = fileRepository.get(uriPath).orElseThrow(() -> new NotFoundException("missing file, path=" + path));
        return Response.ok(resource).type(MediaTypes.getMediaType(resource.path())).build();
    }

    @GET
    public Response index(@QueryParam("path") String path, @QueryParam("query") String query) {
        String uriPath = uriInfo.getPath();
        return directory(uriPath, path, query);
    }

    @Path("/directory/create")
    @GET
    public Response createDirectory(@QueryParam("path") String path) {
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("path", path);
        return Response.ok(Template.of("/file/directory.create.html", bindings)).build();
    }

    private Response directory(String uriPath, String queryPath, String query) {
        Map<String, Object> bindings = Maps.newHashMap();
        String[] pathArr = uriPath.split("\\/");
        if (pathArr.length > 0 && isNumeric(pathArr[pathArr.length - 1])) {
            bindings.put("page", pathArr[pathArr.length - 1]);
        }
        if (queryPath != null) {
            bindings.put("path", queryPath);
        }
        if (query != null) {
            bindings.put("query", query);
        }
        return Response.ok(Template.of("/file/file.html", bindings)).build();
    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
}
