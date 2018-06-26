package io.sited.file.web;

import com.google.common.base.Splitter;
import io.sited.file.api.FileWebService;
import io.sited.file.api.file.BatchDeleteFileRequest;
import io.sited.file.api.file.CreateFileRequest;
import io.sited.file.api.file.FileListQuery;
import io.sited.file.api.file.FileListResponse;
import io.sited.file.api.file.FileQuery;
import io.sited.file.api.file.FileResponse;
import io.sited.file.api.file.UpdateFileRequest;
import io.sited.file.domain.File;
import io.sited.file.service.FileService;
import io.sited.file.service.FileSynchronizeService;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class FileWebServiceImpl implements FileWebService {
    @Inject
    FileService fileService;
    @Inject
    FileSynchronizeService fileSynchronizeService;

    @Override
    public FileResponse get(String id) {
        return response(fileService.get(id));
    }

    @Override
    public Optional<FileResponse> findByPath(String path) {
        Optional<File> file = fileService.findByPath(path);
        return file.map(this::response);
    }

    @Override
    public QueryResponse<FileResponse> find(FileQuery query) {
        return fileService.find(query).map(this::response);
    }

    @Override
    public QueryResponse<FileListResponse> list(FileListQuery query) {
        return fileService.list(query);
    }

    @Override
    public FileResponse create(CreateFileRequest request) {
        return response(fileService.create(request));
    }

    @Override
    public FileResponse update(String id, UpdateFileRequest request) {
        return response(fileService.update(id, request));
    }

    @Override
    public void delete(String id, String requestBy) {
        fileService.delete(id, requestBy);
    }

    @Override
    public void batchDelete(BatchDeleteFileRequest request) {
        request.ids.forEach(id -> fileService.delete(id, request.requestBy));
    }

    @Override
    public void revert(String id, String requestBy) {
        fileService.revert(id, requestBy);
    }

    @Override
    public void synchronize() {
        fileSynchronizeService.synchronize();
    }

    private FileResponse response(File file) {
        FileResponse response = new FileResponse();
        response.id = file.id;
        response.directoryId = file.directoryId;
        response.path = file.path;
        response.fileName = file.fileName;
        response.title = file.title;
        response.length = file.length;
        response.description = file.description;
        response.tags = file.tags == null ? null : Splitter.on(',').splitToList(file.tags);
        response.extension = file.extension;
        response.status = file.status;
        response.createdTime = file.createdTime;
        response.createdBy = file.createdBy;
        response.updatedTime = file.updatedTime;
        response.updatedBy = file.updatedBy;
        return response;
    }
}
