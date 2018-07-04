package io.sited.file.web.web.ajax;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import io.sited.file.api.DirectoryWebService;
import io.sited.file.api.FileRepository;
import io.sited.file.api.FileWebService;
import io.sited.file.api.directory.CreateDirectoryRequest;
import io.sited.file.api.directory.DirectoryResponse;
import io.sited.file.api.file.CreateFileRequest;
import io.sited.file.api.file.FileListQuery;
import io.sited.file.api.file.FileResponse;
import io.sited.file.api.file.UpdateFileRequest;
import io.sited.file.web.service.FileService;
import io.sited.file.web.web.ajax.file.FileListAJAXResponse;
import io.sited.file.web.web.ajax.file.FileUploadAJAXResponse;
import io.sited.file.web.web.ajax.file.ListFileAJAXRequest;
import io.sited.resource.InputStreamResource;
import io.sited.resource.Resource;
import io.sited.resource.ResourceWrapper;
import io.sited.util.collection.QueryResponse;
import io.sited.web.UserInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
@Path("/web/api/file")
public class FileAJAXController {
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
    public FileUploadAJAXResponse upload(@QueryParam("directoryId") String directoryId, @QueryParam("path") String path,
                                         @QueryParam("title") String title, @QueryParam("description") String description,
                                         @FormDataParam("file") InputStream file,
                                         @FormDataParam("file") FormDataContentDisposition fileDisposition) {
        Resource resource = new InputStreamResource(fileDisposition.getName(), file);
        DirectoryResponse directory;
        if (directoryId == null) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            String directoryPath = defaultDirectory(date);
            Optional<DirectoryResponse> directoryOptional = directoryWebService.findByPath(directoryPath);
            if (directoryOptional.isPresent()) {
                directory = directoryOptional.get();
            } else {
                CreateDirectoryRequest createDirectoryRequest = new CreateDirectoryRequest();
                createDirectoryRequest.path = directoryPath;
                createDirectoryRequest.requestBy = userInfo.username();
                directory = directoryWebService.create(createDirectoryRequest);
            }
        } else {
            directory = directoryWebService.get(directoryId);
        }

        if (path == null) {
            String resourcePath = path(directory.path, resource);
            CreateFileRequest createFileRequest = new CreateFileRequest();
            createFileRequest.path = resourcePath;
            createFileRequest.fileName = resource.path();
            createFileRequest.length = resource.length();
            createFileRequest.title = title;
            createFileRequest.description = description;
            createFileRequest.directoryId = directory.id;
            createFileRequest.requestBy = userInfo.username();
            FileResponse response = fileWebService.create(createFileRequest);
            fileRepository.create(new ResourceWrapper(response.path, resource));
            return response(response);
        } else {
            FileResponse fileResponse = fileWebService.findByPath(path).orElseThrow(() -> new NotFoundException(String.format("missing file, path=%s", path)));
            UpdateFileRequest updateFileRequest = new UpdateFileRequest();
            updateFileRequest.fileName = resource.path();
            updateFileRequest.length = resource.length();
            updateFileRequest.title = Strings.isNullOrEmpty(title) ? fileResponse.title : title;
            updateFileRequest.description = Strings.isNullOrEmpty(description) ? fileResponse.description : description;
            updateFileRequest.tags = fileResponse.tags;
            updateFileRequest.requestBy = userInfo.username();
            FileResponse response = fileWebService.update(fileResponse.id, updateFileRequest);
            fileRepository.create(new ResourceWrapper(response.path, resource));
            return response(response);
        }
    }

    String path(String directoryPath, Resource resource) {
        String fileExtension = Files.getFileExtension(resource.path());
        return "/file" + directoryPath + UUID.randomUUID().toString() + '.' + fileExtension;
    }

    String defaultDirectory(String date) {
        return "/upload/" + date + '/';
    }

    private FileUploadAJAXResponse response(FileResponse response) {
        FileUploadAJAXResponse fileUploadAJAXResponse = new FileUploadAJAXResponse();
        fileUploadAJAXResponse.directoryId = response.directoryId;
        fileUploadAJAXResponse.path = response.path;
        return fileUploadAJAXResponse;
    }

}
