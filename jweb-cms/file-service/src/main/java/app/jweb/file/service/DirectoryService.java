package app.jweb.file.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.file.api.directory.CreateDirectoriesRequest;
import app.jweb.file.api.directory.CreateDirectoryRequest;
import app.jweb.file.api.directory.DeleteDirectoryRequest;
import app.jweb.file.api.directory.DirectoryQuery;
import app.jweb.file.api.directory.DirectoryStatus;
import app.jweb.file.api.directory.UpdateDirectoryRequest;
import app.jweb.file.domain.Directory;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.exception.Exceptions;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class DirectoryService {
    @Inject
    Repository<Directory> repository;

    public Directory get(String id) {
        return repository.get(id);
    }

    public Optional<Directory> findByPath(String path) {
        return repository.query("SELECT t FROM Directory t WHERE t.path=?0", path).findOne();
    }

    public QueryResponse<Directory> find(DirectoryQuery query) {
        Query<Directory> dbQuery = repository.query("SELECT t FROM Directory t WHERE 1=1");
        int index = 0;

        if (!Strings.isNullOrEmpty(query.parentId)) {
            dbQuery.append(String.format("AND t.parentId = ?%d", index), query.parentId);
            index++;
        }
        if (query.status != null) {
            dbQuery.append(String.format("AND t.status = ?%d", index), query.status);
        }

        dbQuery.limit(query.page, query.limit);
        if (!Strings.isNullOrEmpty(query.sortingField)) {
            dbQuery.sort("t." + query.sortingField, query.desc);
        } else {
            dbQuery.sort("t.updatedTime", true);
        }
        return dbQuery.findAll();
    }


    public List<Directory> find() {
        return repository.query("SELECT t FROM Directory t").sort("t.updatedTime", true).find();
    }

    public Integer count(String parentId, String query, DirectoryStatus status) {
        Query<Directory> dbQuery = repository.query("SELECT COUNT(t) FROM Directory t WHERE 1=1 ");
        int index = 0;

        if (!Strings.isNullOrEmpty(parentId)) {
            dbQuery.append(String.format("AND t.parentId = ?%d ", index), parentId);
            index++;
        }
        if (!Strings.isNullOrEmpty(query)) {
            dbQuery.append(String.format("AND t.path LIKE ?%d ", index), "%" + query + "%");
            index++;
        }
        if (status != null) {
            dbQuery.append(String.format("AND t.status = ?%d ", index), status);
            index++;
        }

        return (int) dbQuery.count();
    }

    public List<String> children(List<String> parentIds) {
        Query<Directory> dbQuery = repository.query("SELECT t FROM Directory t WHERE t.parentId IN (");
        int index = 0;
        boolean first = true;
        for (String parentId : parentIds) {
            if (first) {
                first = false;
            } else {
                dbQuery.append(",");
            }
            dbQuery.append(String.format("?%d", index), parentId);
            index++;
        }
        dbQuery.append(")");
        return dbQuery.find().stream().map(directory -> directory.id).collect(Collectors.toList());
    }

    public List<Directory> childrenDirectories(List<Directory> parents) {
        Query<Directory> dbQuery = repository.query("SELECT t FROM Directory t WHERE t.parentId IN (");
        int index = 0;
        boolean first = true;
        for (Directory parent : parents) {
            if (first) {
                first = false;
            } else {
                dbQuery.append(",");
            }
            dbQuery.append(String.format("?%d", index), parent.id);
            index++;
        }
        dbQuery.append(")");
        return dbQuery.find();
    }

    @Transactional
    public Directory create(CreateDirectoryRequest request) {
        if (request.path.indexOf("/") != 0) {
            throw Exceptions.badRequestException("path", "invalid directory path");
        }
        if (findByPath(request.path).isPresent()) {
            throw Exceptions.badRequestException("path", "directory is exists");
        }

        Directory directory = new Directory();
        directory.id = UUID.randomUUID().toString();
        directory.parentId = validateParentId(request.parentId);
        if (request.parentId != null) {
            directory.parentIds = Joiner.on(";").join(parentIds(request.parentId));
        } else {
            directory.parentIds = null;
        }
        directory.path = request.path;
        directory.name = name(request.path);
        directory.description = request.description;
        directory.status = DirectoryStatus.ACTIVE;
        directory.ownerId = request.ownerId;
        directory.groupId = request.groupId;
        if (request.groupRoles != null) {
            directory.groupRoles = Joiner.on(";").join(request.groupRoles);
        }
        if (request.ownerRoles != null) {
            directory.ownerRoles = Joiner.on(";").join(request.ownerRoles);
        }
        if (request.othersRoles != null) {
            directory.othersRoles = Joiner.on(";").join(request.othersRoles);
        }

        directory.createdTime = OffsetDateTime.now();
        directory.updatedTime = OffsetDateTime.now();
        directory.createdBy = request.requestBy;
        directory.updatedBy = request.requestBy;
        repository.insert(directory);
        return directory;
    }

    @Transactional
    public Directory create(CreateDirectoriesRequest request) {
        Optional<Directory> directoryOptional = findByPath(request.path);
        if (directoryOptional.isPresent()) {
            return directoryOptional.get();
        }
        List<String> directories = directories(request.path);
        Directory last = null;
        for (String directoryPath : directories) {
            Optional<Directory> current = findByPath(directoryPath);
            if (current.isPresent()) {
                last = current.get();
            } else {
                Directory directory = new Directory();
                directory.id = UUID.randomUUID().toString();
                directory.parentId = last == null ? null : last.id;
                if (last != null) {
                    directory.parentIds = Joiner.on(";").join(parentIds(last));
                }
                directory.path = directoryPath;
                directory.name = name(directoryPath);
                directory.ownerId = request.ownerId;
                directory.groupId = request.groupId;
                if (request.ownerRoles != null) {
                    directory.ownerRoles = Joiner.on(";").join(request.ownerRoles);
                }
                if (request.groupRoles != null) {
                    directory.groupRoles = Joiner.on(";").join(request.groupRoles);
                }
                if (request.othersRoles != null) {
                    directory.othersRoles = Joiner.on(";").join(request.othersRoles);
                }
                directory.status = DirectoryStatus.ACTIVE;
                directory.createdTime = OffsetDateTime.now();
                directory.updatedTime = OffsetDateTime.now();
                directory.createdBy = request.requestBy;
                directory.updatedBy = request.requestBy;
                last = repository.insert(directory);
            }
        }
        return last;
    }

    private String validateParentId(String parentId) {
        if (parentId == null) {
            return parentId;
        }
        Optional<Directory> directory = findById(parentId);
        if (!directory.isPresent()) {
            throw Exceptions.badRequestException("parentId", "parent directory not exists");
        }
        return parentId;
    }

    private Optional<Directory> findById(String parentId) {
        return repository.query("SELECT t FROM Directory t WHERE t.id=?0", parentId).findOne();
    }

    @Transactional
    public Directory update(String id, UpdateDirectoryRequest request) {
        Directory directory = get(id);
        directory.parentId = request.parentId;
        if (request.parentId != null) {
            directory.parentIds = Joiner.on(";").join(parentIds(request.parentId));
        } else {
            directory.parentIds = null;
        }
        if (!Strings.isNullOrEmpty(request.description)) {
            directory.description = request.description;
        }
        if (!Strings.isNullOrEmpty(request.ownerId)) {
            directory.ownerId = request.ownerId;
        }
        if (request.ownerRoles != null) {
            directory.ownerRoles = Joiner.on(";").join(request.ownerRoles);
        }
        if (!Strings.isNullOrEmpty(request.groupId)) {
            directory.groupId = request.groupId;
        }
        if (request.groupRoles != null) {
            directory.groupRoles = Joiner.on(";").join(request.groupRoles);
        }
        if (request.othersRoles != null) {
            directory.othersRoles = Joiner.on(";").join(request.othersRoles);
        }
        directory.updatedTime = OffsetDateTime.now();
        directory.updatedBy = request.requestBy;
        repository.update(id, directory);
        return directory;
    }

    @Transactional
    public void delete(DeleteDirectoryRequest request) {
        repository.batchDelete(request.ids);
        repository.batchDelete(children(request.ids));
    }

    private List<String> directories(String path) {
        if (Strings.isNullOrEmpty(path) || !path.startsWith("/upload/") || !path.endsWith("/")) {
            throw Exceptions.badRequestException("path", "invalid directory path, path=" + path);
        }
        List<String> directories = Lists.newArrayList();
        for (int i = "/upload".length(); i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '/') {
                directories.add(path.substring(0, i + 1));
            }
        }
        return directories;
    }

    private String name(String path) {
        int p = path.lastIndexOf("/", path.length() - 2);
        if (p >= 0) {
            return path.substring(p + 1, path.length() - 1);
        }
        return path;
    }

    private List<String> parentIds(String parentId) {
        Directory parent = get(parentId);
        List<String> parentIds = Lists.newLinkedList();
        if (parent.parentIds != null) {
            parentIds.addAll(Splitter.on(";").splitToList(parent.parentIds));
            parentIds.add(parentId);
        } else {
            parentIds.add(0, parent.id);
            while (parent.parentId != null) {
                parentIds.add(0, parent.parentId);
                parent = get(parent.parentId);
            }
        }
        return parentIds;
    }

    private List<String> parentIds(Directory parent) {
        List<String> parentIds = Lists.newLinkedList();
        if (parent.parentIds != null) {
            parentIds.addAll(Splitter.on(";").splitToList(parent.parentIds));
            parentIds.add(parent.id);
        } else {
            parentIds.add(0, parent.id);
            Directory temp = parent;
            while (temp.parentId != null) {
                parentIds.add(0, temp.parentId);
                temp = get(temp.parentId);
            }
        }
        return parentIds;
    }

    public List<Directory> roots() {
        return repository.query("SELECT t FROM Directory t WHERE t.parentId IS NULL").find();
    }

    public List<Directory> allChildrenDirectories(List<String> parentIds) {
        if (parentIds.isEmpty()) {
            return ImmutableList.of();
        }
        Query<Directory> query = repository.query("SELECT t FROM Directory t WHERE (");
        int index = 0;
        for (int i = 0; i < parentIds.size(); i++) {
            if (i != 0) {
                query.append(" OR ");
            }
            query.append(String.format("t.parentIds LIKE ?%d", index), "%" + parentIds.get(i) + "%");
            index++;
        }
        query.append(String.format(") AND t.status=?%d", index), DirectoryStatus.ACTIVE);
        return query.find();
    }
}
