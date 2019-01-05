package app.jweb.page.web;


import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import app.jweb.page.api.PageCategoryWebService;
import app.jweb.page.api.category.CategoryNodeResponse;
import app.jweb.page.api.category.CategoryQuery;
import app.jweb.page.api.category.CategoryResponse;
import app.jweb.page.api.category.CategoryTreeQuery;
import app.jweb.page.api.category.CreateCategoryRequest;
import app.jweb.page.api.category.DeleteCategoryRequest;
import app.jweb.page.api.category.UpdateCategoryRequest;
import app.jweb.page.domain.PageCategory;
import app.jweb.page.service.PageCategoryService;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageCategoryWebServiceImpl implements PageCategoryWebService {
    @Inject
    PageCategoryService pageCategoryService;

    @Override
    public CategoryResponse get(String id) {
        return response(pageCategoryService.get(id));
    }

    @Override
    public List<CategoryResponse> parents(String categoryId) {
        PageCategory pageCategory = pageCategoryService.get(categoryId);
        if (pageCategory.parentIds == null) {
            return ImmutableList.of();
        }
        List<String> categoryIds = Splitter.on(";").splitToList(pageCategory.parentIds);
        Map<String, CategoryResponse> categories = pageCategoryService.batchGet(categoryIds).stream().map(this::response).collect(Collectors.toMap(category -> category.id, category -> category));
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
        List<PageCategory> children = pageCategoryService.children(id);
        return children.stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public QueryResponse<CategoryResponse> find(CategoryQuery query) {
        return pageCategoryService.find(query).map(this::response);
    }

    @Override
    public Optional<CategoryResponse> findByPath(String path) {
        return pageCategoryService.findByPath(path).map(this::response);
    }

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {
        PageCategory pageCategory = pageCategoryService.create(request);
        return response(pageCategory);
    }

    @Override
    public CategoryResponse update(String id, UpdateCategoryRequest request) {
        PageCategory pageCategory = pageCategoryService.update(id, request);
        return response(pageCategory);
    }

    @Override
    public void delete(DeleteCategoryRequest request) {
        for (String id : request.ids) {
            pageCategoryService.delete(id, request.requestBy);
        }
    }

    @Override
    public List<CategoryNodeResponse> tree(CategoryTreeQuery query) {
        List<PageCategory> list = pageCategoryService.find(query);
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
//        List<PageCategory> list = pageCategoryService.descendant(Lists.newArrayList(id));
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
//        List<PageCategory> list = pageCategoryService.descendant(categoryIds);
//        list.addAll(pageCategoryService.batchGet(categoryIds));
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

    private CategoryResponse response(PageCategory pageCategory) {
        CategoryResponse response = new CategoryResponse();
        response.id = pageCategory.id;
        response.parentId = pageCategory.parentId;
        response.parentIds = pageCategory.parentIds == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.parentIds);
        response.templatePath = pageCategory.templatePath;
        response.path = pageCategory.path;
        response.level = pageCategory.level;
        response.displayName = pageCategory.displayName;
        response.description = pageCategory.description;
        response.imageURL = pageCategory.imageURL;
        response.keywords = Strings.isNullOrEmpty(pageCategory.keywords) ? Lists.newArrayList() : Splitter.on(';').splitToList(pageCategory.keywords);
        response.tags = Strings.isNullOrEmpty(pageCategory.tags) ? Lists.newArrayList() : Splitter.on(';').splitToList(pageCategory.tags);
        response.fields = pageCategory.fields == null ? ImmutableMap.of() : JSON.fromJSON(pageCategory.fields, Map.class);
        response.displayOrder = pageCategory.displayOrder;
        response.ownerId = pageCategory.ownerId;
        response.ownerRoles = Strings.isNullOrEmpty(pageCategory.ownerRoles) ? Lists.newArrayList() : Splitter.on(';').splitToList(pageCategory.ownerRoles);
        response.groupId = pageCategory.groupId;
        response.groupRoles = Strings.isNullOrEmpty(pageCategory.groupRoles) ? Lists.newArrayList() : Splitter.on(';').splitToList(pageCategory.groupRoles);
        response.othersRoles = Strings.isNullOrEmpty(pageCategory.othersRoles) ? Lists.newArrayList() : Splitter.on(';').splitToList(pageCategory.othersRoles);
        response.createdTime = pageCategory.createdTime;
        response.createdBy = pageCategory.createdBy;
        response.updatedTime = pageCategory.updatedTime;
        response.updatedBy = pageCategory.updatedBy;
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

    private CategoryNodeResponse node(PageCategory pageCategory) {
        CategoryNodeResponse node = new CategoryNodeResponse();
        node.id = pageCategory.id;
        node.parentId = pageCategory.parentId;
        node.parentIds = pageCategory.parentIds == null ? Lists.newArrayList() : Splitter.on(";").splitToList(pageCategory.parentIds);
        node.templatePath = pageCategory.templatePath;
        node.path = pageCategory.path;
        node.level = pageCategory.level;
        node.description = pageCategory.description;
        node.imageURL = pageCategory.imageURL;
        node.displayName = pageCategory.displayName;
        node.keywords = pageCategory.keywords == null ? ImmutableList.of() : Splitter.on(';').splitToList(pageCategory.tags);
        node.tags = pageCategory.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(pageCategory.tags);
        node.fields = pageCategory.fields == null ? ImmutableMap.of() : JSON.fromJSON(pageCategory.fields, Map.class);
        node.displayOrder = pageCategory.displayOrder;
        node.status = pageCategory.status;
        node.ownerId = pageCategory.ownerId;
        node.ownerRoles = Strings.isNullOrEmpty(pageCategory.ownerRoles) ? ImmutableList.of() : Splitter.on(';').splitToList(pageCategory.ownerRoles);
        node.groupId = pageCategory.groupId;
        node.groupRoles = Strings.isNullOrEmpty(pageCategory.groupRoles) ? ImmutableList.of() : Splitter.on(';').splitToList(pageCategory.groupRoles);
        node.othersRoles = Strings.isNullOrEmpty(pageCategory.othersRoles) ? ImmutableList.of() : Splitter.on(';').splitToList(pageCategory.othersRoles);
        node.createdTime = pageCategory.createdTime;
        node.createdBy = pageCategory.createdBy;
        node.updatedTime = pageCategory.updatedTime;
        node.updatedBy = pageCategory.updatedBy;
        return node;
    }
}
