package app.jweb.file.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.file.api.directory.DirectoryQuery;
import app.jweb.file.api.directory.DirectoryStatus;
import app.jweb.file.api.file.DeleteFileRequest;
import app.jweb.file.api.file.CreateFileRequest;
import app.jweb.file.api.file.FileListQuery;
import app.jweb.file.api.file.FileListResponse;
import app.jweb.file.api.file.FileQuery;
import app.jweb.file.api.file.FileStatus;
import app.jweb.file.api.file.UpdateFileRequest;
import app.jweb.file.domain.Directory;
import app.jweb.file.domain.File;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.exception.Exceptions;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class FileService {
    @Inject
    Repository<File> repository;
    @Inject
    DirectoryService directoryService;

    public File get(String id) {
        return repository.get(id);
    }

    public QueryResponse<File> find(FileQuery query) {
        Query<File> dbQuery = repository.query("SELECT t FROM File t WHERE 1=1");
        int index = 0;
        if (query.directoryIds != null && !query.directoryIds.isEmpty()) {
            dbQuery.append("AND t.directoryId IN (");
            for (int i = 0; i < query.directoryIds.size(); i++) {
                if (i != 0) {
                    dbQuery.append(",");
                }
                dbQuery.append(String.format("?%d", index), query.directoryIds.get(i));
                index++;
            }
            dbQuery.append(")");
        }

        if (!Strings.isNullOrEmpty(query.keywords)) {
            String keywords = '%' + query.keywords + '%';
            dbQuery.append(String.format("AND (t.title LIKE ?%d OR t.description LIKE ?%d OR t.tags LIKE ?%d) ", index, index, index), keywords, keywords, keywords);
            index++;
        }

        if (query.startUpdatedTime != null) {
            dbQuery.append(String.format("AND t.updatedTime>?%d ", index), query.startUpdatedTime);
            index++;
        }

        if (query.endUpdatedTime != null) {
            dbQuery.append(String.format("AND t.updatedTime<=?%d ", index), query.endUpdatedTime);
            index++;
        }

        if (query.status != null) {
            dbQuery.append(String.format("AND t.status=?%d", index), query.status);
            index++;
        }

        dbQuery.limit(query.page, query.limit);
        if (!Strings.isNullOrEmpty(query.sortingField)) {
            dbQuery.sort(query.sortingField, query.desc);
        } else {
            dbQuery.sort("t.updatedTime", true);
        }
        return dbQuery.findAll();
    }

    public QueryResponse<FileListResponse> list(FileListQuery query) {
        Integer directoryCount = directoryService.count(query.directoryId, query.query, DirectoryStatus.ACTIVE);
        Integer fileCount = count(query.directoryId, query.query, FileStatus.ACTIVE);

        DirectoryQuery directoryQuery = new DirectoryQuery();
        directoryQuery.parentId = query.directoryId;
        directoryQuery.query = query.query;
        directoryQuery.status = DirectoryStatus.ACTIVE;
        directoryQuery.limit = query.limit;
        directoryQuery.sortingField = query.sortingField;
        directoryQuery.desc = query.desc;

        FileQuery fileQuery = new FileQuery();
        fileQuery.keywords = query.query;
        if (!Strings.isNullOrEmpty(query.directoryId)) {
            fileQuery.directoryIds = Lists.newArrayList(query.directoryId);
        }
        fileQuery.status = FileStatus.ACTIVE;
        fileQuery.limit = query.limit;
        fileQuery.sortingField = query.sortingField;
        fileQuery.desc = query.desc;
        QueryResponse<FileListResponse> responses = new QueryResponse<>();
        responses.total = (long) (directoryCount + fileCount);
        responses.page = query.page;
        responses.limit = query.limit;

        int directoryMaxPage = (int) Math.ceil((double) directoryCount / query.limit);
        if (directoryCount >= query.page * query.limit) {
            directoryQuery.page = query.page;
            responses.items = directoryService.find(directoryQuery).map(this::fileListResponse).items;
        } else if (directoryCount > (query.page - 1) * query.limit) {
            directoryQuery.page = directoryMaxPage;
            responses.items = directoryService.find(directoryQuery).map(this::fileListResponse).items;
            fileQuery.page = 1;
            fileQuery.limit = query.limit - responses.items.size();
            responses.items.addAll(find(fileQuery).map(this::fileListResponse).items);
        } else {
            fileQuery.page = query.page - directoryMaxPage;
            responses.items = find(fileQuery).map(this::fileListResponse).items;
        }
        return responses;
    }

    private FileListResponse fileListResponse(File request) {
        FileListResponse instance = new FileListResponse();
        instance.id = request.id;
        instance.path = request.path;
        instance.isDirectory = false;
        instance.description = request.description;
        instance.createdTime = request.createdTime;
        instance.createdBy = request.createdBy;
        instance.updatedTime = request.updatedTime;
        instance.updatedBy = request.updatedBy;
        return instance;
    }

    private FileListResponse fileListResponse(Directory request) {
        FileListResponse instance = new FileListResponse();
        instance.id = request.id;
        instance.path = request.path;
        instance.isDirectory = true;
        instance.description = request.description;
        instance.createdTime = request.createdTime;
        instance.createdBy = request.createdBy;
        instance.updatedTime = request.updatedTime;
        instance.updatedBy = request.updatedBy;
        return instance;
    }

    public Optional<File> findByPath(String path) {
        return repository.query("SELECT t FROM File t WHERE t.path = ?0", path).findOne();
    }

    @Transactional
    public File create(CreateFileRequest request) {
        File file = new File();
        file.id = UUID.randomUUID().toString();
        file.title = request.title;
        file.directoryId = request.directoryId;
        file.path = request.path;
        file.length = request.length;
        file.fileName = request.fileName;
        file.extension = Files.getFileExtension(request.path);
        file.description = request.description;
        file.tags = request.tags == null ? null : Joiner.on(',').skipNulls().join(request.tags);
        file.status = FileStatus.ACTIVE;
        file.updatedBy = request.requestBy;
        file.createdTime = OffsetDateTime.now();
        file.createdBy = request.requestBy;
        file.updatedTime = OffsetDateTime.now();
        repository.insert(file);
        return file;
    }

    @Transactional
    public File update(String id, UpdateFileRequest request) {
        File file = get(id);
        if (!Strings.isNullOrEmpty(request.title)) {
            file.title = request.title;
        }
        if (!Strings.isNullOrEmpty(request.description)) {
            file.description = request.description;
        }
        if (request.tags != null) {
            file.tags = Joiner.on(',').skipNulls().join(request.tags);
        }
        file.updatedBy = request.requestBy;
        file.updatedTime = OffsetDateTime.now();
        repository.update(id, file);
        return file;
    }

    @Transactional
    public void batchDelete(DeleteFileRequest request) {
        List<File> files = repository.batchGet(request.ids);
        for (File file : files) {
            repository.delete(file.id);
        }
    }

    @Transactional
    public void revert(String id, String requestBy) {
        File file = get(id);
        if (!file.status.equals(FileStatus.INACTIVE)) {
            throw Exceptions.badRequestException("id", "file.error.invalidFileStatus");
        }
        file.status = FileStatus.ACTIVE;
        file.updatedBy = requestBy;
        file.updatedTime = OffsetDateTime.now();
        repository.update(id, file);
    }

    private int count(String directoryId, String query, FileStatus status) {
        Query<File> dbQuery = repository.query("SELECT t FROM File t WHERE 1=1");
        int index = 0;
        if (directoryId != null) {
            dbQuery.append(String.format("AND t.directoryId = ?%d", index), directoryId);
            index++;
        }

        if (!Strings.isNullOrEmpty(query)) {
            String likeQuery = '%' + query + '%';
            dbQuery.append(String.format("AND (t.path LIKE ?%d OR t.title LIKE ?%d OR t.description LIKE ?%d OR t.tags LIKE ?%d) ", index, index, index, index), likeQuery, likeQuery, likeQuery, likeQuery);
            index++;
        }

        if (status != null) {
            dbQuery.append(String.format("AND t.status=?%d", index), status);
            index++;
        }

        return (int) dbQuery.count();
    }
}
