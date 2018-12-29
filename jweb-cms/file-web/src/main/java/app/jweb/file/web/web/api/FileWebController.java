package app.jweb.file.web.web.api;

import app.jweb.file.api.directory.CreateDirectoriesRequest;
import app.jweb.file.api.directory.DirectoryResponse;
import app.jweb.file.web.web.api.file.FileListAJAXResponse;
import app.jweb.file.web.web.api.file.FileUploadAJAXResponse;
import app.jweb.file.web.web.api.file.ListFileAJAXRequest;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import app.jweb.ApplicationException;
import app.jweb.file.api.DirectoryWebService;
import app.jweb.file.api.FileRepository;
import app.jweb.file.api.FileWebService;
import app.jweb.file.api.file.CreateFileRequest;
import app.jweb.file.api.file.FileListQuery;
import app.jweb.file.api.file.FileResponse;
import app.jweb.file.api.file.UpdateFileRequest;
import app.jweb.file.web.service.FileService;
import app.jweb.resource.InputStreamResource;
import app.jweb.resource.Resource;
import app.jweb.resource.ResourceWrapper;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.exception.Exceptions;
import app.jweb.web.UserInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author chi
 */
@Path("/web/api/file")
public class FileWebController {
    @Inject
    FileWebService fileWebService;
    @Inject
    FileRepository fileRepository;
    @Inject
    DirectoryWebService directoryWebService;
    @Inject
    FileService fileService;
    @Inject
    UserInfo userInfo;

    @POST
    public QueryResponse<FileListAJAXResponse> find(ListFileAJAXRequest listFileAJAXRequest) {
        FileListQuery query = new FileListQuery();
        query.directoryId = listFileAJAXRequest.parentId;
        query.query = listFileAJAXRequest.query;
        query.limit = listFileAJAXRequest.limit;
        query.page = listFileAJAXRequest.page;
        query.sortingField = listFileAJAXRequest.sortingField;
        query.desc = listFileAJAXRequest.desc;
        return fileService.list(query);
    }

    @Path("/{id}")
    @DELETE
    public void delete(@PathParam("id") String id) {
        fileWebService.delete(id, userInfo.username());
    }

    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@FormDataParam("directoryId") String directoryId, @FormDataParam("directoryId") String directoryPath, @FormDataParam("path") String path,
                           @FormDataParam("title") String title, @FormDataParam("description") String description,
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

    private FileUploadAJAXResponse response(FileResponse response) {
        FileUploadAJAXResponse fileUploadAJAXResponse = new FileUploadAJAXResponse();
        fileUploadAJAXResponse.directoryId = response.directoryId;
        fileUploadAJAXResponse.path = response.path;
        return fileUploadAJAXResponse;
    }

}
