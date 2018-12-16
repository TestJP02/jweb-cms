package app.jweb.post.web;


import app.jweb.post.api.PostCategoryWebService;
import app.jweb.post.api.category.CategoryNodeResponse;
import app.jweb.post.api.category.CategoryQuery;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.post.api.category.CategoryTreeQuery;
import app.jweb.post.api.category.CreateCategoryRequest;
import app.jweb.post.api.category.DeleteCategoryRequest;
import app.jweb.post.api.category.UpdateCategoryRequest;
import app.jweb.post.domain.PostCategory;
import app.jweb.post.service.PostCategoryService;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PostCategoryWebServiceImpl implements PostCategoryWebService {
    @Inject
    PostCategoryService postCategoryService;

    @Override
    public CategoryResponse get(String id) {
        return response(postCategoryService.get(id));
    }

    @Override
    public List<CategoryResponse> parents(String categoryId) {
        PostCategory postCategory = postCategoryService.get(categoryId);
        if (postCategory.parentIds == null) {
            return ImmutableList.of();
        }
        List<String> categoryIds = Splitter.on(";").splitToList(postCategory.parentIds);
        Map<String, CategoryResponse> categories = postCategoryService.batchGet(categoryIds).stream().map(this::response).collect(Collectors.toMap(category -> category.id, category -> category));
        List<CategoryResponse> list = Lists.newArrayList();
        for (String id : categoryIds) {
            CategoryResponse category = categories.get(id);
            if (category != null) {
                list.add(category);
            }
        }
        return list;
    }

    @Override
    public List<CategoryResponse> children(String id) {
        List<PostCategory> children = postCategoryService.children(id);
        return children.stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public QueryResponse<CategoryResponse> find(CategoryQuery query) {
        return postCategoryService.find(query).map(this::response);
    }

    @Override
    public Optional<CategoryResponse> findByPath(String path) {
        return postCategoryService.findByPath(path).map(this::response);
    }

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {
        PostCategory postCategory = postCategoryService.create(request);
        return response(postCategory);
    }

    @Override
    public CategoryResponse update(String id, UpdateCategoryRequest request) {
        PostCategory postCategory = postCategoryService.update(id, request);
        return response(postCategory);
    }

    @Override
    public void delete(DeleteCategoryRequest request) {
        for (String id : request.ids) {
            postCategoryService.delete(id, request.requestBy);
        }
    }

    @Override
    public List<CategoryNodeResponse> tree(CategoryTreeQuery query) {
        List<PostCategory> list = postCategoryService.find(query);
        Map<String, CategoryNodeResponse> index = Maps.newHashMap();
        List<CategoryNodeResponse> firstLevels = Lists.newArrayList();
        list.forEach(category -> {
            CategoryNodeResponse node = node(category);
            node.children = Lists.newArrayList();
            if (Objects.equals(category.parentId, query.parentId)) {
                firstLevels.add(node);
            }
            index.put(node.id, node);
        });
        buildTree(index);
        return firstLevels;
    }
//
//
//    @Override
//    public List<CategoryNodeResponse> subTree(String id) {
//        List<CategoryNodeResponse> firstLevels = Lists.newArrayList();
//        List<PostCategory> list = postCategoryService.descendant(Lists.newArrayList(id));
//        Map<String, CategoryNodeResponse> index = Maps.newHashMap();
//        list.forEach(category -> {
//            if (!index.containsKey(category.id)) {
//                CategoryNodeResponse node = node(category);
//                node.children = Lists.newArrayList();
//                if (Objects.equals(node.parentId, id)) {
//                    firstLevels.add(node);
//                }
//                index.put(node.id, node);
//            }
//        });
//        buildTree(index);
//        return firstLevels;
//    }
//
//    @Override
//    public List<CategoryNodeResponse> subTree(List<String> categoryIds) {
//        if (categoryIds.isEmpty()) {
//            return ImmutableList.of();
//        }
//        String parentId = categoryIds.get(0);
//        List<CategoryNodeResponse> firstLevels = Lists.newArrayList();
//        List<PostCategory> list = postCategoryService.descendant(categoryIds);
//        list.addAll(postCategoryService.batchGet(categoryIds));
//        Map<String, CategoryNodeResponse> index = Maps.newHashMap();
//        list.forEach(category -> {
//            if (!index.containsKey(category.id)) {
//                CategoryNodeResponse node = node(category);
//                node.children = Lists.newArrayList();
//                if (Objects.equals(node.parentId, parentId)) {
//                    firstLevels.add(node);
//                }
//                index.put(node.id, node);
//            }
//        });
//        buildTree(index);
//        return firstLevels;
//    }

    private CategoryResponse response(PostCategory postCategory) {
        CategoryResponse response = new CategoryResponse();
        response.id = postCategory.id;
        response.parentId = postCategory.parentId;
        response.parentIds = postCategory.parentIds == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.parentIds);
        response.templatePath = postCategory.templatePath;
        response.path = postCategory.path;
        response.level = postCategory.level;
        response.displayName = postCategory.displayName;
        response.description = postCategory.description;
        response.imageURL = postCategory.imageURL;
        response.keywords = Strings.isNullOrEmpty(postCategory.keywords) ? Lists.newArrayList() : Splitter.on(';').splitToList(postCategory.keywords);
        response.tags = Strings.isNullOrEmpty(postCategory.tags) ? Lists.newArrayList() : Splitter.on(';').splitToList(postCategory.tags);
        response.fields = postCategory.fields == null ? ImmutableMap.of() : JSON.fromJSON(postCategory.fields, Map.class);
        response.displayOrder = postCategory.displayOrder;
        response.ownerId = postCategory.ownerId;
        response.ownerRoles = Strings.isNullOrEmpty(postCategory.ownerRoles) ? Lists.newArrayList() : Splitter.on(';').splitToList(postCategory.ownerRoles);
        response.groupId = postCategory.groupId;
        response.groupRoles = Strings.isNullOrEmpty(postCategory.groupRoles) ? Lists.newArrayList() : Splitter.on(';').splitToList(postCategory.groupRoles);
        response.othersRoles = Strings.isNullOrEmpty(postCategory.othersRoles) ? Lists.newArrayList() : Splitter.on(';').splitToList(postCategory.othersRoles);
        response.createdTime = postCategory.createdTime;
        response.createdBy = postCategory.createdBy;
        response.updatedTime = postCategory.updatedTime;
        response.updatedBy = postCategory.updatedBy;
        return response;
    }

    private void buildTree(Map<String, CategoryNodeResponse> index) {
        index.values().forEach(category -> {
            if (category.parentId != null) {
                CategoryNodeResponse parent = index.get(category.parentId);
                if (parent != null) {
                    parent.children.add(index.get(category.id));
                }
            }
        });
    }

    private CategoryNodeResponse node(PostCategory postCategory) {
        CategoryNodeResponse node = new CategoryNodeResponse();
        node.id = postCategory.id;
        node.parentId = postCategory.parentId;
        node.parentIds = postCategory.parentIds == null ? Lists.newArrayList() : Splitter.on(";").splitToList(postCategory.parentIds);
        node.templatePath = postCategory.templatePath;
        node.path = postCategory.path;
        node.level = postCategory.level;
        node.description = postCategory.description;
        node.imageURL = postCategory.imageURL;
        node.displayName = postCategory.displayName;
        node.keywords = postCategory.keywords == null ? ImmutableList.of() : Splitter.on(';').splitToList(postCategory.tags);
        node.tags = postCategory.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(postCategory.tags);
        node.fields = postCategory.fields == null ? ImmutableMap.of() : JSON.fromJSON(postCategory.fields, Map.class);
        node.displayOrder = postCategory.displayOrder;
        node.status = postCategory.status;
        node.ownerId = postCategory.ownerId;
        node.ownerRoles = Strings.isNullOrEmpty(postCategory.ownerRoles) ? ImmutableList.of() : Splitter.on(';').splitToList(postCategory.ownerRoles);
        node.groupId = postCategory.groupId;
        node.groupRoles = Strings.isNullOrEmpty(postCategory.groupRoles) ? ImmutableList.of() : Splitter.on(';').splitToList(postCategory.groupRoles);
        node.othersRoles = Strings.isNullOrEmpty(postCategory.othersRoles) ? ImmutableList.of() : Splitter.on(';').splitToList(postCategory.othersRoles);
        node.createdTime = postCategory.createdTime;
        node.createdBy = postCategory.createdBy;
        node.updatedTime = postCategory.updatedTime;
        node.updatedBy = postCategory.updatedBy;
        return node;
    }
}
