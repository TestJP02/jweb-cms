package app.jweb.post.service;


import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.message.MessagePublisher;
import app.jweb.post.api.category.CategoryCreatedMessage;
import app.jweb.post.api.category.CategoryDeletedMessage;
import app.jweb.post.api.category.CategoryQuery;
import app.jweb.post.api.category.CategoryStatus;
import app.jweb.post.api.category.CategoryTreeQuery;
import app.jweb.post.api.category.CategoryUpdatedMessage;
import app.jweb.post.api.category.CreateCategoryRequest;
import app.jweb.post.api.category.UpdateCategoryRequest;
import app.jweb.post.domain.PostCategory;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.exception.Exceptions;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PostCategoryService {
    @Inject
    Repository<PostCategory> repository;
    @Inject
    MessagePublisher<CategoryCreatedMessage> categoryCreatedMessageMessagePublisher;
    @Inject
    MessagePublisher<CategoryUpdatedMessage> categoryUpdatedMessageMessagePublisher;
    @Inject
    MessagePublisher<CategoryDeletedMessage> categoryDeletedMessageMessagePublisher;

    public PostCategory get(String id) {
        return repository.get(id);
    }

    public List<PostCategory> batchGet(List<String> categoryIds) {
        return repository.batchGet(categoryIds);
    }

    public List<PostCategory> find() {
        return repository.query("SELECT t FROM PostCategory t").sort("t.displayOrder").find();
    }

    public List<PostCategory> find(CategoryTreeQuery categoryTreeQuery) {
        Query<PostCategory> query = repository.query("SELECT t FROM PostCategory t WHERE 1=1");
        int index = 0;
        if (categoryTreeQuery.status != null) {
            query.append("AND t.status=?" + index++, categoryTreeQuery.status);
        }
        if (categoryTreeQuery.level != null) {
            query.append("AND t.level<=?" + index++, categoryTreeQuery.level);
        }
        if (categoryTreeQuery.parentId != null) {
            PostCategory parent = get(categoryTreeQuery.parentId);
            query.append("AND t.parentIds LIKE ?" + index, parent.parentIds + '%');
        }
        return query.find();
    }

    public QueryResponse<PostCategory> find(CategoryQuery categoryQuery) {
        Query<PostCategory> query = repository.query("SELECT t FROM PostCategory t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(categoryQuery.query)) {
            query.append("AND t.name=?" + index++, categoryQuery.query);
        }
        if (categoryQuery.level != null) {
            query.append("AND t.level=?" + index++, categoryQuery.level);
        }
        if (categoryQuery.parentId != null) {
            query.append("AND t.parentId=?" + index, categoryQuery.parentId);
        }
        return query.limit(categoryQuery.page, categoryQuery.limit).findAll();
    }

    public List<String> childrenIds(String categoryId) {
        List<String> ids = Lists.newArrayList();
        ids.add(categoryId);
        List<PostCategory> children = repository.query("SELECT t FROM PostCategory t WHERE t.parentIds LIKE ?0 AND t.status=?1", "%" + categoryId + "%", CategoryStatus.ACTIVE).find();
        for (PostCategory child : children) {
            ids.add(child.id);
        }
        return ids;
    }

    public Optional<PostCategory> findById(String id) {
        return repository.query("SELECT t FROM PostCategory t WHERE t.id=?0", id).findOne();
    }

    public Optional<PostCategory> findByPath(String categoryPath) {
        return repository.query("SELECT t FROM PostCategory t WHERE t.path=?0 AND t.status=?1", categoryPath, CategoryStatus.ACTIVE).findOne();
    }

    @Transactional
    public PostCategory create(CreateCategoryRequest request) {
        if (request.parentId != null) {
            Optional<PostCategory> category = findById(request.parentId);
            if (!category.isPresent()) {
                throw Exceptions.badRequestException("parentId", "parent category doesn't exist");
            }
        }
        PostCategory postCategory = new PostCategory();
        postCategory.id = UUID.randomUUID().toString();
        postCategory.parentId = request.parentId;
        if (request.parentId != null) {
            PostCategory parent = get(request.parentId);
            postCategory.parentIds = Joiner.on(";").join(parentIds(parent));
            postCategory.level = parent.level + 1;
        } else {
            postCategory.parentIds = null;
            postCategory.level = 1;
        }
        postCategory.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        postCategory.templatePath = request.templatePath;
        postCategory.path = request.path;
        postCategory.displayName = request.displayName;
        postCategory.description = request.description;
        postCategory.imageURL = request.imageURL;
        postCategory.displayOrder = request.displayOrder;
        postCategory.keywords = request.keywords == null ? null : Joiner.on(';').join(request.keywords);
        postCategory.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
        postCategory.status = CategoryStatus.ACTIVE;
        postCategory.ownerId = request.ownerId;
        postCategory.ownerRoles = request.ownerRoles == null ? null : Joiner.on(';').join(request.ownerRoles);
        postCategory.groupId = request.groupId;
        postCategory.groupRoles = request.groupRoles == null ? null : Joiner.on(';').join(request.groupRoles);
        postCategory.othersRoles = request.othersRoles == null ? null : Joiner.on(';').join(request.othersRoles);
        postCategory.updatedTime = OffsetDateTime.now();
        postCategory.updatedBy = request.requestBy;
        postCategory.createdTime = OffsetDateTime.now();
        postCategory.createdBy = request.requestBy;
        repository.insert(postCategory);
        notifyPostCreated(postCategory);
        return postCategory;
    }

    private List<String> parentIds(PostCategory parent) {
        List<String> parentIds = Lists.newLinkedList();
        if (parent.parentIds != null) {
            parentIds.addAll(Splitter.on(";").splitToList(parent.parentIds));
            parentIds.add(parent.id);
        } else {
            parentIds.add(0, parent.id);
            PostCategory parentCategory = parent;
            while (parentCategory.parentId != null) {
                parentIds.add(0, parentCategory.parentId);
                parentCategory = get(parentCategory.parentId);
            }
        }
        return parentIds;
    }

    @Transactional
    public PostCategory update(String id, UpdateCategoryRequest request) {
        PostCategory postCategory = get(id);
        postCategory.parentId = request.parentId;
        if (request.parentId != null) {
            PostCategory parent = get(request.parentId);
            postCategory.parentIds = Joiner.on(";").join(parentIds(parent));
            postCategory.level = parent.level + 1;
        } else {
            postCategory.parentIds = null;
            postCategory.level = 1;
        }
        postCategory.path = request.path;
        postCategory.templatePath = request.templatePath;
        postCategory.displayName = request.displayName;
        postCategory.displayOrder = request.displayOrder;
        postCategory.description = request.description;
        postCategory.imageURL = request.imageURL;
        postCategory.keywords = request.keywords == null ? null : Joiner.on(';').join(request.keywords);
        postCategory.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
        postCategory.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        postCategory.ownerId = request.ownerId;
        postCategory.ownerRoles = request.ownerRoles == null ? null : Joiner.on(';').join(request.ownerRoles);
        postCategory.groupId = request.groupId;
        postCategory.groupRoles = request.groupRoles == null ? null : Joiner.on(';').join(request.groupRoles);
        postCategory.othersRoles = request.othersRoles == null ? null : Joiner.on(';').join(request.othersRoles);
        postCategory.updatedTime = OffsetDateTime.now();
        postCategory.updatedBy = request.requestBy;
        repository.update(postCategory.id, postCategory);
        notifyPostUpdated(postCategory);
        return postCategory;
    }

    private void notifyPostCreated(PostCategory postCategory) {
        CategoryCreatedMessage message = new CategoryCreatedMessage();
        message.id = postCategory.id;
        message.parentId = postCategory.parentId;
        message.parentIds = postCategory.parentIds == null ? Lists.newArrayList() : Splitter.on(";").splitToList(postCategory.parentIds);
        message.templatePath = postCategory.templatePath;
        message.path = postCategory.path;
        message.displayName = postCategory.displayName;
        message.description = postCategory.description;
        message.imageURL = postCategory.imageURL;
        message.displayOrder = postCategory.displayOrder;
        message.keywords = postCategory.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.keywords);
        message.tags = postCategory.tags == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.tags);
        message.fields = postCategory.fields == null ? ImmutableMap.of() : JSON.fromJSON(postCategory.fields, Map.class);
        message.status = postCategory.status;
        message.ownerId = postCategory.ownerId;
        message.ownerRoles = postCategory.ownerRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.ownerRoles);
        message.groupId = postCategory.groupId;
        message.groupRoles = postCategory.groupRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.groupRoles);
        message.othersRoles = postCategory.othersRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.othersRoles);
        message.updatedTime = OffsetDateTime.now();
        message.updatedBy = postCategory.updatedBy;
        message.createdTime = OffsetDateTime.now();
        message.createdBy = postCategory.createdBy;
        categoryCreatedMessageMessagePublisher.publish(message);
    }

    private void notifyPostUpdated(PostCategory postCategory) {
        CategoryUpdatedMessage message = new CategoryUpdatedMessage();
        message.id = postCategory.id;
        message.parentId = postCategory.parentId;
        message.parentIds = postCategory.parentIds == null ? Lists.newArrayList() : Splitter.on(";").splitToList(postCategory.parentIds);
        message.templatePath = postCategory.templatePath;
        message.path = postCategory.path;
        message.displayName = postCategory.displayName;
        message.description = postCategory.description;
        message.imageURL = postCategory.imageURL;
        message.displayOrder = postCategory.displayOrder;
        message.keywords = postCategory.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.keywords);
        message.tags = postCategory.tags == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.tags);
        message.fields = postCategory.fields == null ? ImmutableMap.of() : JSON.fromJSON(postCategory.fields, Map.class);
        message.status = postCategory.status;
        message.ownerId = postCategory.ownerId;
        message.ownerRoles = postCategory.ownerRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.ownerRoles);
        message.groupId = postCategory.groupId;
        message.groupRoles = postCategory.groupRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.groupRoles);
        message.othersRoles = postCategory.othersRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.othersRoles);
        message.updatedTime = OffsetDateTime.now();
        message.updatedBy = postCategory.updatedBy;
        message.createdTime = OffsetDateTime.now();
        message.createdBy = postCategory.createdBy;
        categoryUpdatedMessageMessagePublisher.publish(message);
    }

    private void notifyPostDeleted(PostCategory postCategory) {
        CategoryDeletedMessage message = new CategoryDeletedMessage();
        message.id = postCategory.id;
        message.parentId = postCategory.parentId;
        message.parentIds = postCategory.parentIds == null ? Lists.newArrayList() : Splitter.on(";").splitToList(postCategory.parentIds);
        message.templatePath = postCategory.templatePath;
        message.path = postCategory.path;
        message.displayName = postCategory.displayName;
        message.description = postCategory.description;
        message.imageURL = postCategory.imageURL;
        message.displayOrder = postCategory.displayOrder;
        message.keywords = postCategory.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.keywords);
        message.tags = postCategory.tags == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.tags);
        message.fields = postCategory.fields == null ? ImmutableMap.of() : JSON.fromJSON(postCategory.fields, Map.class);
        message.status = postCategory.status;
        message.ownerId = postCategory.ownerId;
        message.ownerRoles = postCategory.ownerRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.ownerRoles);
        message.groupId = postCategory.groupId;
        message.groupRoles = postCategory.groupRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.groupRoles);
        message.othersRoles = postCategory.othersRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(postCategory.othersRoles);
        message.updatedTime = OffsetDateTime.now();
        message.updatedBy = postCategory.updatedBy;
        message.createdTime = OffsetDateTime.now();
        message.createdBy = postCategory.createdBy;
        categoryDeletedMessageMessagePublisher.publish(message);
    }

    @Transactional
    public void delete(String id, String requestBy) {
        PostCategory postCategory = get(id);
        if (postCategory.status.equals(CategoryStatus.INACTIVE)) {
            //pageService.deleteByCategoryId(id);
            repository.delete(id);
            deleteChildren(id, requestBy);
        } else {
            //pageService.inactiveByCategoryId(id, requestBy);
            postCategory.status = CategoryStatus.INACTIVE;
            postCategory.updatedBy = requestBy;
            postCategory.updatedTime = OffsetDateTime.now();
            repository.update(postCategory.id, postCategory);
            inactiveChildren(postCategory.id, requestBy);

            notifyPostDeleted(postCategory);
        }
    }

    @Transactional
    private void inactiveChildren(String parentId, String requestBy) {
        List<PostCategory> categories = repository.query("SELECT t FROM PostCategory t WHERE t.parentId=?0", parentId).find();
        if (!categories.isEmpty()) {
            categories.forEach(child -> {
                if (child.status != CategoryStatus.INACTIVE) {
                    delete(child.id, requestBy);
                }
            });
        }
    }

    @Transactional
    private void deleteChildren(String parentId, String requestBy) {
        List<PostCategory> categories = repository.query("SELECT t FROM PostCategory t WHERE t.parentId=?0", parentId).find();
        if (!categories.isEmpty()) {
            categories.forEach(child -> {
                delete(child.id, requestBy);
            });
        }
    }

    public List<PostCategory> children(String parentId) {
        return repository.query("SELECT t FROM PostCategory t WHERE t.parentId=?0 AND t.status=?1", parentId, CategoryStatus.ACTIVE).sort("t.displayOrder", true).find();
    }
}
