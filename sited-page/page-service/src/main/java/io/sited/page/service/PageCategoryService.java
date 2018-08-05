package io.sited.page.service;


import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.message.MessagePublisher;
import io.sited.page.api.category.CategoryChangedMessage;
import io.sited.page.api.category.CategoryQuery;
import io.sited.page.api.category.CategoryStatus;
import io.sited.page.api.category.CategoryTreeQuery;
import io.sited.page.api.category.CreateCategoryRequest;
import io.sited.page.api.category.UpdateCategoryRequest;
import io.sited.page.domain.PageCategory;
import io.sited.util.JSON;
import io.sited.util.collection.QueryResponse;
import io.sited.util.exception.Exceptions;

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
    MessagePublisher<CategoryChangedMessage> publisher;

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
        if (categoryTreeQuery.parentId != null) {
            query.append("AND t.parentIds LIKE ?" + index, '%' + categoryTreeQuery.parentId + '%');
        }
        return query.find();
    }

    public QueryResponse<PageCategory> find(CategoryQuery categoryQuery) {
        Query<PageCategory> query = repository.query("SELECT t FROM PageCategory t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(categoryQuery.query)) {
            query.append("AND t.name=?" + index++, categoryQuery.query);
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

    public List<PageCategory> descendant(List<String> categoryIds) {
        if (categoryIds.isEmpty()) {
            return ImmutableList.of();
        }
        Query<PageCategory> query = repository.query("SELECT t FROM PageCategory t WHERE");
        for (int i = 0; i < categoryIds.size(); i++) {
            if (i != 0) {
                query.append(" OR ");
            }
            query.append("t.parentIds LIKE ?" + i, "%" + categoryIds.get(i) + "%");
        }
        query.append(" AND t.status=?" + categoryIds.size(), CategoryStatus.ACTIVE);
        return query.find();
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
            pageCategory.parentIds = Joiner.on(";").join(parentIds(request.parentId));
        } else {
            pageCategory.parentIds = null;
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
        nofityCategoryChanged(pageCategory);
        return pageCategory;
    }

    private List<String> parentIds(String categoryId) {
        PageCategory parent = get(categoryId);
        List<String> parentIds = Lists.newLinkedList();
        if (parent.parentIds != null) {
            parentIds.addAll(Splitter.on(";").splitToList(parent.parentIds));
            parentIds.add(categoryId);
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
            pageCategory.parentIds = Joiner.on(";").join(parentIds(request.parentId));
        } else {
            pageCategory.parentIds = null;
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
        nofityCategoryChanged(pageCategory);
        return pageCategory;
    }

    private void nofityCategoryChanged(PageCategory pageCategory) {
        CategoryChangedMessage message = new CategoryChangedMessage();
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
        publisher.publish(message);
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

    public List<PageCategory> children(List<PageCategory> parents) {
        if (parents.isEmpty()) {
            return ImmutableList.of();
        }
        Query<PageCategory> dbQuery = repository.query("SELECT t FROM PageCategory t WHERE t.parentId IN (");
        boolean first = true;
        for (int i = 0; i < parents.size(); i++) {
            PageCategory parent = parents.get(i);
            if (first) {
                first = false;
            } else {
                dbQuery.append(",");
            }
            dbQuery.append("?" + i, parent.id);
        }
        dbQuery.append(")");
        return dbQuery.find();
    }

    public List<PageCategory> roots() {
        return repository.query("SELECT t FROM PageCategory t WHERE t.parentId IS NULL").find();
    }

}
