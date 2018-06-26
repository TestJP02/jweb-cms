package io.sited.file.web.service;

import io.sited.file.api.FileWebService;
import io.sited.file.api.file.FileListQuery;
import io.sited.file.api.file.FileListResponse;
import io.sited.file.web.web.ajax.file.FileListAJAXResponse;
import io.sited.util.collection.QueryResponse;

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
