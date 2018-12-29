package app.jweb.file.web.service;

import app.jweb.file.api.file.FileListQuery;
import app.jweb.file.web.web.api.file.FileListAJAXResponse;
import app.jweb.file.api.FileWebService;
import app.jweb.file.api.file.FileListResponse;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;

/**
 * @author chi
 */
public class FileService {
    @Inject
    FileWebService fileWebService;

    public QueryResponse<FileListAJAXResponse> list(FileListQuery query) {
        return fileWebService.list(query).map(this::response);
    }

    private FileListAJAXResponse response(FileListResponse request) {
        FileListAJAXResponse instance = new FileListAJAXResponse();
        instance.id = request.id;
        instance.path = request.path;
        instance.description = request.description;
        instance.isDirectory = request.isDirectory;
        instance.createdTime = request.createdTime;
        instance.createdBy = request.createdBy;
        instance.updatedTime = request.updatedTime;
        instance.updatedBy = request.updatedBy;
        return instance;
    }
}
