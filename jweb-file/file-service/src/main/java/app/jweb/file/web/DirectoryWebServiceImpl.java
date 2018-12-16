package app.jweb.file.web;

import app.jweb.file.api.directory.CreateDirectoriesRequest;
import app.jweb.file.api.directory.DirectoryNodeResponse;
import app.jweb.file.api.directory.DirectoryQuery;
import app.jweb.file.api.directory.DirectoryResponse;
import app.jweb.file.api.directory.UpdateDirectoryRequest;
import app.jweb.file.domain.Directory;
import app.jweb.file.service.DirectoryService;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import app.jweb.file.api.DirectoryWebService;
import app.jweb.file.api.directory.CreateDirectoryRequest;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author chi
 */
public class DirectoryWebServiceImpl implements DirectoryWebService {
    @Inject
    DirectoryService directoryService;

    @Override
    public DirectoryResponse get(String id) {
        return response(directoryService.get(id));
    }

    @Override
    public Optional<DirectoryResponse> findByPath(String path) {
        return directoryService.findByPath(path).map(this::response);
    }

    @Override
    public QueryResponse<DirectoryResponse> find(DirectoryQuery query) {
        return directoryService.find(query).map(this::response);
    }

    @Override
    public List<String> children(String id) {
        return children(Lists.newArrayList(id));
    }

    private List<String> children(List<String> parentIds) {
        List<String> currentChildren = directoryService.children(parentIds);
        if (!currentChildren.isEmpty()) {
            currentChildren.addAll(children(currentChildren));
        }
        return currentChildren;
    }

    @Override
    public List<DirectoryNodeResponse> firstTwoLevels() {
        List<Directory> roots = directoryService.roots();
        List<Directory> directories = directoryService.childrenDirectories(roots);
        List<DirectoryNodeResponse> result = Lists.newArrayList();
        for (Directory root : roots) {
            DirectoryNodeResponse node = new DirectoryNodeResponse();
            node.directory = response(root);
            node.children = Lists.newArrayList();
            for (Directory child : directories) {
                if (root.id.equals(child.parentId)) {
                    DirectoryNodeResponse childNode = new DirectoryNodeResponse();
                    childNode.directory = response(child);
                    childNode.children = Lists.newArrayList();
                    node.children.add(childNode);
                }
            }
            result.add(node);
        }
        return result;
    }

    @Override
    public List<DirectoryNodeResponse> subTree(String id) {
        List<DirectoryNodeResponse> firstLevels = Lists.newArrayList();
        List<Directory> list = directoryService.allChildrenDirectories(Lists.newArrayList(id));
        Map<String, DirectoryNodeResponse> index = Maps.newHashMap();
        list.forEach(directory -> {
            if (!index.containsKey(directory.id)) {
                DirectoryNodeResponse node = new DirectoryNodeResponse();
                node.directory = response(directory);
                node.children = Lists.newArrayList();
                if (Objects.equals(directory.parentId, id)) {
                    firstLevels.add(node);
                }
                index.put(directory.id, node);
            }
        });
        buildTree(index);
        return firstLevels;
    }

    private void buildTree(Map<String, DirectoryNodeResponse> index) {
        index.values().forEach(node -> {
            if (node.directory.parentId != null) {
                DirectoryNodeResponse parent = index.get(node.directory.parentId);
                if (parent != null) {
                    parent.children.add(index.get(node.directory.id));
                }
            }
        });
    }

    @Override
    public List<DirectoryNodeResponse> tree() {
        return tree(directoryService.find());
    }

    private List<DirectoryNodeResponse> tree(List<Directory> directories) {
        Map<String, DirectoryNodeResponse> index = new HashMap<>();
        List<DirectoryNodeResponse> firstLevel = new ArrayList<>();

        directories.forEach(directory -> {
            DirectoryNodeResponse node = new DirectoryNodeResponse();
            node.directory = response(directory);
            node.children = Lists.newArrayList();
            index.put(directory.id, node);

            if (directory.parentId == null) {
                firstLevel.add(node);
            }
        });

        directories.forEach(directory -> {
            if (directory.parentId != null && index.containsKey(directory.parentId)) {
                DirectoryNodeResponse parent = index.get(directory.parentId);
                if (parent.children == null) {
                    parent.children = new ArrayList<>();
                }
                parent.children.add(index.get(directory.id));
            }
        });
        return firstLevel;
    }

    @Override
    public DirectoryResponse create(CreateDirectoryRequest request) {
        return response(directoryService.create(request));
    }

    @Override
    public DirectoryResponse createDirectories(CreateDirectoriesRequest request) {
        Directory directory = directoryService.create(request);
        return response(directory);
    }

    @Override
    public DirectoryResponse update(String id, UpdateDirectoryRequest request) {
        return response(directoryService.update(id, request));
    }

    @Override
    public void delete(String id, String requestBy) {
        directoryService.delete(id, requestBy);
    }

    private DirectoryResponse response(Directory directory) {
        DirectoryResponse response = new DirectoryResponse();
        response.id = directory.id;
        response.parentId = directory.parentId;
        response.parentIds = directory.parentIds == null ? ImmutableList.of() : Splitter.on(";").splitToList(directory.parentIds);
        response.path = directory.path;
        response.name = directory.name;
        response.description = directory.description;
        response.status = directory.status;
        response.ownerId = directory.ownerId;
        response.ownerRoles = directory.ownerRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(directory.ownerRoles);
        response.groupId = directory.groupId;
        response.groupRoles = directory.groupRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(directory.groupRoles);
        response.othersRoles = directory.othersRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(directory.othersRoles);
        response.createdTime = directory.createdTime;
        response.updatedTime = directory.updatedTime;
        response.createdBy = directory.createdBy;
        response.updatedBy = directory.updatedBy;
        return response;
    }
}
