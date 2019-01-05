package app.jweb.page.service;


import app.jweb.page.domain.PageCategory;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.message.MessagePublisher;
import app.jweb.page.api.category.CategoryCreatedMessage;
import app.jweb.page.api.category.CategoryDeletedMessage;
import app.jweb.page.api.category.CategoryQuery;
import app.jweb.page.api.category.CategoryStatus;
import app.jweb.page.api.category.CategoryTreeQuery;
import app.jweb.page.api.category.CategoryUpdatedMessage;
import app.jweb.page.api.category.CreateCategoryRequest;
import app.jweb.page.api.category.UpdateCategoryRequest;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.exception.Exceptions;

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
public class PageCategoryService {
    @Inject
    Repository<PageCategory> repository;
    @Inject
    MessagePublisher<CategoryCreatedMessage> categoryCreatedMessageMessagePublisher;
    @Inject
    MessagePublisher<CategoryUpdatedMessage> categoryUpdatedMessageMessagePublisher;
    @Inject
    MessagePublisher<CategoryDeletedMessage> categoryDeletedMessageMessagePublisher;

    public PageCategory get(String id) {
        return repository.get(id);
    }

    public List<PageCategory> batchGet(List<String> categoryIds) {
        return repository.batchGet(categoryIds);
    }

    public List<PageCategory> find() {
        return repository.query("SELECT t FROM PageCategory t").sort("t.displayOrder").find();
    }

    public List<PageCategory> find(CategoryTreeQuery categoryTreeQuery) {
        Query<PageCategory> query = repository.query("SELECT t FROM PageCategory t WHERE 1=1");
        int index = 0;
        if (categoryTreeQuery.status != null) {
            query.append("AND t.status=?" + index++, categoryTreeQuery.status);
        }
        if (categoryTreeQuery.level != null) {
            query.append("AND t.level<=?" + index++, categoryTreeQuery.level);
        }
        if (categoryTreeQuery.parentId != null) {
            PageCategory parent = get(categoryTreeQuery.parentId);
            query.append("AND t.parentIds LIKE ?" + index, parent.parentIds + '%');
        }
        return query.find();
    }

    public QueryResponse<PageCategory> find(CategoryQuery categoryQuery) {
        Query<PageCategory> query = repository.query("SELECT t FROM PageCategory t WHERE 1=1");
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
        List<PageCategory> children = repository.query("SELECT t FROM PageCategory t WHERE t.parentIds LIKE ?0 AND t.status=?1", "%" + categoryId + "%", CategoryStatus.ACTIVE).find();
        for (PageCategory child : children) {
            ids.add(child.id);
        }
        return ids;
    }

    public Optional<PageCategory> findById(String id) {
        return repository.query("SELECT t FROM PageCategory t WHERE t.id=?0", id).findOne();
    }

    public Optional<PageCategory> findByPath(String categoryPath) {
        return repository.query("SELECT t FROM PageCategory t WHERE t.path=?0 AND t.status=?1", categoryPath, CategoryStatus.ACTIVE).findOne();
    }

    @Transactional
    public PageCategory create(CreateCategoryRequest request) {
        if (request.parentId != null) {
            Optional<PageCategory> category = findById(request.parentId);
            if (!category.isPresent()) {
                throw Exceptions.badRequestException("parentId", "parent category doesn't exist");
            }
        }
        PageCategory pageCategory = new PageCategory();
        pageCategory.id = UUID.randomUUID().toString();
        pageCategory.parentId = request.parentId;
        if (request.parentId != null) {
            PageCategory parent = get(request.parentId);
            pageCategory.parentIds = Joiner.on(";").join(parentIds(parent));
            pageCategory.level = parent.level + 1;
        } else {
            pageCategory.parentIds = null;
            pageCategory.level = 1;
        }
        pageCategory.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        pageCategory.templatePath = request.templatePath;
        pageCategory.path = request.path;
        pageCategory.displayName = request.displayName;
        pageCategory.description = request.description;
        pageCategory.imageURL = request.imageURL;
        pageCategory.displayOrder = request.displayOrder;
        pageCategory.keywords = request.keywords == null ? null : Joiner.on(';').join(request.keywords);
        pageCategory.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
        pageCategory.status = CategoryStatus.ACTIVE;
        pageCategory.ownerId = request.ownerId;
        pageCategory.ownerRoles = request.ownerRoles == null ? null : Joiner.on(';').join(request.ownerRoles);
        pageCategory.groupId = request.groupId;
        pageCategory.groupRoles = request.groupRoles == null ? null : Joiner.on(';').join(request.groupRoles);
        pageCategory.othersRoles = request.othersRoles == null ? null : Joiner.on(';').join(request.othersRoles);
        pageCategory.updatedTime = OffsetDateTime.now();
        pageCategory.updatedBy = request.requestBy;
        pageCategory.createdTime = OffsetDateTime.now();
        pageCategory.createdBy = request.requestBy;
        repository.insert(pageCategory);
        notifyPageCreated(pageCategory);
        return pageCategory;
    }

    private List<String> parentIds(PageCategory parent) {
        List<String> parentIds = Lists.newLinkedList();
        if (parent.parentIds != null) {
            parentIds.addAll(Splitter.on(";").splitToList(parent.parentIds));
            parentIds.add(parent.id);
        } else {
            parentIds.add(0, parent.id);
            while (parent.parentId != null) {
                parentIds.add(0, parent.parentId);
                parent = get(parent.parentId);
            }
        }
        return parentIds;
    }

    @Transactional
    public PageCategory update(String id, UpdateCategoryRequest request) {
        PageCategory pageCategory = get(id);
        pageCategory.parentId = request.parentId;
        if (request.parentId != null) {
            PageCategory parent = get(request.parentId);
            pageCategory.parentIds = Joiner.on(";").join(parentIds(parent));
            pageCategory.level = parent.level + 1;
        } else {
            pageCategory.parentIds = null;
            pageCategory.level = 1;
        }
        pageCategory.path = request.path;
        pageCategory.templatePath = request.templatePath;
        pageCategory.displayName = request.displayName;
        pageCategory.displayOrder = request.displayOrder;
        pageCategory.description = request.description;
        pageCategory.imageURL = request.imageURL;
        pageCategory.keywords = request.keywords == null ? null : Joiner.on(';').join(request.keywords);
        pageCategory.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
        pageCategory.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        pageCategory.ownerId = request.ownerId;
        pageCategory.ownerRoles = request.ownerRoles == null ? null : Joiner.on(';').join(request.ownerRoles);
        pageCategory.groupId = request.groupId;
        pageCategory.groupRoles = request.groupRoles == null ? null : Joiner.on(';').join(request.groupRoles);
        pageCategory.othersRoles = request.othersRoles == null ? null : Joiner.on(';').join(request.othersRoles);
        pageCategory.updatedTime = OffsetDateTime.now();
        pageCategory.updatedBy = request.requestBy;
        repository.update(pageCategory.id, pageCategory);
        notifyPageUpdated(pageCategory);
        return pageCategory;
    }

    private void notifyPageCreated(PageCategory pageCategory) {
        CategoryCreatedMessage message = new CategoryCreatedMessage();
        message.id = pageCategory.id;
        message.parentId = pageCategory.parentId;
        message.parentIds = pageCategory.parentIds == null ? Lists.newArrayList() : Splitter.on(";").splitToList(pageCategory.parentIds);
        message.templatePath = pageCategory.templatePath;
        message.path = pageCategory.path;
        message.displayName = pageCategory.displayName;
        message.description = pageCategory.description;
        message.imageURL = pageCategory.imageURL;
        message.displayOrder = pageCategory.displayOrder;
        message.keywords = pageCategory.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.keywords);
        message.tags = pageCategory.tags == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.tags);
        message.fields = pageCategory.fields == null ? ImmutableMap.of() : JSON.fromJSON(pageCategory.fields, Map.class);
        message.status = pageCategory.status;
        message.ownerId = pageCategory.ownerId;
        message.ownerRoles = pageCategory.ownerRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.ownerRoles);
        message.groupId = pageCategory.groupId;
        message.groupRoles = pageCategory.groupRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.groupRoles);
        message.othersRoles = pageCategory.othersRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.othersRoles);
        message.updatedTime = OffsetDateTime.now();
        message.updatedBy = pageCategory.updatedBy;
        message.createdTime = OffsetDateTime.now();
        message.createdBy = pageCategory.createdBy;
        categoryCreatedMessageMessagePublisher.publish(message);
    }

    private void notifyPageUpdated(PageCategory pageCategory) {
        CategoryUpdatedMessage message = new CategoryUpdatedMessage();
        message.id = pageCategory.id;
        message.parentId = pageCategory.parentId;
        message.parentIds = pageCategory.parentIds == null ? Lists.newArrayList() : Splitter.on(";").splitToList(pageCategory.parentIds);
        message.templatePath = pageCategory.templatePath;
        message.path = pageCategory.path;
        message.displayName = pageCategory.displayName;
        message.description = pageCategory.description;
        message.imageURL = pageCategory.imageURL;
        message.displayOrder = pageCategory.displayOrder;
        message.keywords = pageCategory.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.keywords);
        message.tags = pageCategory.tags == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.tags);
        message.fields = pageCategory.fields == null ? ImmutableMap.of() : JSON.fromJSON(pageCategory.fields, Map.class);
        message.status = pageCategory.status;
        message.ownerId = pageCategory.ownerId;
        message.ownerRoles = pageCategory.ownerRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.ownerRoles);
        message.groupId = pageCategory.groupId;
        message.groupRoles = pageCategory.groupRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.groupRoles);
        message.othersRoles = pageCategory.othersRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.othersRoles);
        message.updatedTime = OffsetDateTime.now();
        message.updatedBy = pageCategory.updatedBy;
        message.createdTime = OffsetDateTime.now();
        message.createdBy = pageCategory.createdBy;
        categoryUpdatedMessageMessagePublisher.publish(message);
    }

    private void notifyPageDeleted(PageCategory pageCategory) {
        CategoryDeletedMessage message = new CategoryDeletedMessage();
        message.id = pageCategory.id;
        message.parentId = pageCategory.parentId;
        message.parentIds = pageCategory.parentIds == null ? Lists.newArrayList() : Splitter.on(";").splitToList(pageCategory.parentIds);
        message.templatePath = pageCategory.templatePath;
        message.path = pageCategory.path;
        message.displayName = pageCategory.displayName;
        message.description = pageCategory.description;
        message.imageURL = pageCategory.imageURL;
        message.displayOrder = pageCategory.displayOrder;
        message.keywords = pageCategory.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.keywords);
        message.tags = pageCategory.tags == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.tags);
        message.fields = pageCategory.fields == null ? ImmutableMap.of() : JSON.fromJSON(pageCategory.fields, Map.class);
        message.status = pageCategory.status;
        message.ownerId = pageCategory.ownerId;
        message.ownerRoles = pageCategory.ownerRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.ownerRoles);
        message.groupId = pageCategory.groupId;
        message.groupRoles = pageCategory.groupRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.groupRoles);
        message.othersRoles = pageCategory.othersRoles == null ? ImmutableList.of() : Splitter.on(";").splitToList(pageCategory.othersRoles);
        message.updatedTime = OffsetDateTime.now();
        message.updatedBy = pageCategory.updatedBy;
        message.createdTime = OffsetDateTime.now();
        message.createdBy = pageCategory.createdBy;
        categoryDeletedMessageMessagePublisher.publish(message);
    }

    @Transactional
    public void delete(String id, String requestBy) {
        PageCategory pageCategory = get(id);
        if (pageCategory.status.equals(CategoryStatus.INACTIVE)) {
            //pageService.deleteByCategoryId(id);
            repository.delete(id);
            deleteChildren(id, requestBy);
        } else {
            //pageService.inactiveByCategoryId(id, requestBy);
            pageCategory.status = CategoryStatus.INACTIVE;
            pageCategory.updatedBy = requestBy;
            pageCategory.updatedTime = OffsetDateTime.now();
            repository.update(pageCategory.id, pageCategory);
            inactiveChildren(pageCategory.id, requestBy);

            notifyPageDeleted(pageCategory);
        }
    }

    @Transactional
    private void inactiveChildren(String parentId, String requestBy) {
        List<PageCategory> categories = repository.query("SELECT t FROM PageCategory t WHERE t.parentId=?0", parentId).find();
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
        List<PageCategory> categories = repository.query("SELECT t FROM PageCategory t WHERE t.parentId=?0", parentId).find();
        if (!categories.isEmpty()) {
            categories.forEach(child -> {
                delete(child.id, requestBy);
            });
        }
    }

    public List<PageCategory> children(String parentId) {
        return repository.query("SELECT t FROM PageCategory t WHERE t.parentId=?0 AND t.status=?1", parentId, CategoryStatus.ACTIVE).sort("t.displayOrder", true).find();
    }
}
