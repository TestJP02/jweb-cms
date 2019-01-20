package app.jweb.file.admin.web.api;

import app.jweb.ApplicationException;
import app.jweb.file.admin.web.api.file.BatchDeleteAJAXRequest;
import app.jweb.file.admin.web.api.file.FileAJAXResponse;
import app.jweb.file.admin.web.api.file.FileFindAJAXRequest;
import app.jweb.file.admin.web.api.file.UpdateFileAJAXRequest;
import app.jweb.file.api.DirectoryWebService;
import app.jweb.file.api.FileRepository;
import app.jweb.file.api.FileWebService;
import app.jweb.file.api.directory.CreateDirectoriesRequest;
import app.jweb.file.api.directory.DirectoryResponse;
import app.jweb.file.api.file.CreateFileRequest;
import app.jweb.file.api.file.DeleteFileRequest;
import app.jweb.file.api.file.FileQuery;
import app.jweb.file.api.file.FileResponse;
import app.jweb.file.api.file.UpdateFileRequest;
import app.jweb.resource.InputStreamResource;
import app.jweb.resource.Resource;
import app.jweb.resource.ResourceWrapper;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.exception.Exceptions;
import app.jweb.web.UserInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author chi
 */
@Path("/admin/api/file")
public class FileAdminWebController {
    @Inject
    FileWebService fileWebService;
    @Inject
    FileRepository fileRepository;
    @Inject
    DirectoryWebService directoryWebService;
    @Inject
    UserInfo userInfo;

    @RolesAllowed("GET")
    @Path("/{id}")
    @GET
    public FileAJAXResponse get(@PathParam("id") String id) {
        return response(fileWebService.get(id));
    }

    @RolesAllowed("LIST")
    @Path("/find")
    @PUT
    public QueryResponse<FileAJAXResponse> find(FileFindAJAXRequest request) {
        FileQuery instance = new FileQuery();
        if (!Strings.isNullOrEmpty(request.directoryId)) {
            instance.directoryIds = Lists.newArrayList(request.directoryId);
            List<String> children = directoryWebService.children(request.directoryId);
            if (!children.isEmpty()) {
                instance.directoryIds.addAll(children);
            }
        }
        instance.keywords = request.keywords;
        instance.status = request.status;
        instance.page = request.page;
        instance.limit = request.limit;
        return fileWebService.find(instance).map(this::response);
    }

    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@QueryParam("directoryId") String directoryId, @QueryParam("path") String path,
                           @QueryParam("directoryPath") String directoryPath,
                           @QueryParam("title") String title, @QueryParam("description") String description,
                           @FormDataParam("file") InputStream file,
                           @FormDataParam("file") FormDataContentDisposition fileDisposition) throws IOException {
        Resource resource = new InputStreamResource(fileDisposition.getFileName(), file);
        DirectoryResponse directory = directory(directoryId, directoryPath);

        if (path == null) {
            String fullPath = path(directory.path, resource);
            String resourcePath = fullPath.substring(1);
            fileRepository.create(new ResourceWrapper(resourcePath, resource));

            Resource fileResource = fileRepository.get(resourcePath).orElseThrow(() -> new ApplicationException("get file resource error"));
            CreateFileRequest createFileRequest = new CreateFileRequest();
            createFileRequest.path = "/file" + fullPath;
            createFileRequest.fileName = fileDisposition.getFileName();
            createFileRequest.length = fileResource.length();
            createFileRequest.title = title;
            createFileRequest.description = description;
            createFileRequest.directoryId = directory.id;
            createFileRequest.requestBy = userInfo.username();
            FileResponse response = fileWebService.create(createFileRequest);
            return Response.ok(response(response)).type(MediaType.APPLICATION_JSON).build();
        } else {
            FileResponse fileResponse = fileWebService.findByPath(path).orElseThrow(() -> new NotFoundException("missing file, path=" + path));
            String fullPath = fileResponse.path;
            String resourcePath = fullPath.substring(1);
            fileRepository.create(new ResourceWrapper(resourcePath, resource));

            Resource fileResource = fileRepository.get(resourcePath).orElseThrow(() -> new ApplicationException("get file resource error"));
            UpdateFileRequest updateFileRequest = new UpdateFileRequest();
            updateFileRequest.fileName = fileDisposition.getFileName();
            updateFileRequest.length = fileResource.length();
            updateFileRequest.title = Strings.isNullOrEmpty(fileResponse.title) ? null : fileResponse.title;
            updateFileRequest.description = Strings.isNullOrEmpty(fileResponse.description) ? null : fileResponse.description;
            updateFileRequest.tags = fileResponse.tags;
            updateFileRequest.requestBy = userInfo.username();
            FileResponse response = fileWebService.update(fileResponse.id, updateFileRequest);
            return Response.ok(response(response)).type(MediaType.APPLICATION_JSON).build();
        }
    }

    private DirectoryResponse directory(String directoryId, String directoryPath) {
        if (!Strings.isNullOrEmpty(directoryId)) {
            return directoryWebService.get(directoryId);
        }
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String path = Strings.isNullOrEmpty(directoryPath) ? "/upload/" + date + '/' : validateDirectoryPath(directoryPath + date + '/');
        CreateDirectoriesRequest request = new CreateDirectoriesRequest();
        request.path = path;
        return directoryWebService.createDirectories(request);
    }

    private String validateDirectoryPath(String directoryPath) {
        if (Strings.isNullOrEmpty(directoryPath) || directoryPath.charAt(0) != '/') {
            throw Exceptions.badRequestException("directoryPath", "invalid directory path");
        }
        return directoryPath;
    }

    String path(String directoryPath, Resource resource) {
        String fileExtension = Files.getFileExtension(resource.path());
        return directoryPath + UUID.randomUUID().toString() + '.' + fileExtension;
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public FileAJAXResponse update(@PathParam("id") String id, UpdateFileAJAXRequest updateFileAJAXRequest) {
        UpdateFileRequest instance = new UpdateFileRequest();
        instance.title = updateFileAJAXRequest.title;
        instance.description = updateFileAJAXRequest.description;
        instance.tags = updateFileAJAXRequest.tags;
        instance.requestBy = userInfo.username();
        return response(fileWebService.update(id, instance));
    }

    @RolesAllowed("DELETE")
    @Path("/delete")
    @POST
    public void batchDelete(BatchDeleteAJAXRequest batchDeleteAJAXRequest) {
        DeleteFileRequest deleteFileRequest = new DeleteFileRequest();
        deleteFileRequest.ids = batchDeleteAJAXRequest.ids;
        deleteFileRequest.requestBy = userInfo.username();
        fileWebService.batchDelete(deleteFileRequest);
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}/revert")
    @PUT
    public void revert(@PathParam("id") String id) {
        fileWebService.revert(id, userInfo.username());
    }

    @RolesAllowed("UPDATE")
    @Path("/synchronize")
    @GET
    public void synchronize() {
        fileWebService.synchronize();
    }

    private FileAJAXResponse response(FileResponse response) {
        FileAJAXResponse fileAJAXResponse = new FileAJAXResponse();
        fileAJAXResponse.id = response.id;
        fileAJAXResponse.directoryId = response.directoryId;
        fileAJAXResponse.path = response.path;
        fileAJAXResponse.fileName = response.fileName;
        fileAJAXResponse.length = response.length;
        fileAJAXResponse.title = response.title;
        fileAJAXResponse.description = response.description;
        fileAJAXResponse.tags = Objects.requireNonNullElseGet(response.tags, Lists::newArrayList);
        fileAJAXResponse.extension = response.extension;
        fileAJAXResponse.status = response.status;
        fileAJAXResponse.createdTime = response.createdTime;
        fileAJAXResponse.createdBy = response.createdBy;
        fileAJAXResponse.updatedTime = response.updatedTime;
        fileAJAXResponse.updatedBy = response.updatedBy;
        return fileAJAXResponse;
    }
}
