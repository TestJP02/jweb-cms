package io.sited.page.service;


import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.message.MessagePublisher;
import io.sited.page.api.page.CreatePageRequest;
import io.sited.page.api.page.LatestPageQuery;
import io.sited.page.api.page.PageChangedMessage;
import io.sited.page.api.page.PageDeletedMessage;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageRelatedQuery;
import io.sited.page.api.page.PageStatus;
import io.sited.page.domain.Page;
import io.sited.page.domain.PageDraft;
import io.sited.page.util.Markdown;
import io.sited.page.util.URLs;
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
public class PageService {
    @Inject
    Repository<Page> repository;
    @Inject
    PageDraftService pageDraftService;
    @Inject
    PageContentService pageContentService;
    @Inject
    MessagePublisher<PageDeletedMessage> pageDeletePublisher;
    @Inject
    MessagePublisher<PageChangedMessage> pageChangedMessagePublisher;
    @Inject
    PageCategoryService pageCategoryService;

    public Page get(String id) {
        return repository.get(id);
    }

    public List<Page> batchGet(List<String> ids) {
        return repository.batchGet(ids);
    }

    public Optional<Page> find(String path, String language) {
        return repository.query("SELECT t from Page t WHERE t.path=?0 AND t.language=?1 AND t.status=?2", path, language, PageStatus.ACTIVE).findOne();
    }

    public QueryResponse<Page> find(PageQuery pageQuery) {
        int index = 0;
        Query<Page> query = repository.query("SELECT t from Page t WHERE 1=1");
        if (!Strings.isNullOrEmpty(pageQuery.query)) {
            query.append("AND (t.title LIKE ?" + index++ + " OR t.path LIKE ?" + index++ + ")", "%" + pageQuery.query + "%", "%" + pageQuery.query + "%");
        }
        if (!Strings.isNullOrEmpty(pageQuery.categoryId)) {
            List<String> ids = pageCategoryService.childrenIds(pageQuery.categoryId);
            query.append("AND t.categoryId IN (");

            for (int i = 0; i < ids.size(); i++) {
                String categoryId = ids.get(i);
                if (i != 0) {
                    query.append(",");
                }
                query.append("?" + index++, categoryId);
            }
            query.append(")");
        }

        if (pageQuery.userId != null) {
            query.append("AND t.userId=?" + index++, pageQuery.userId);
        }

        if (pageQuery.status != null) {
            query.append("AND t.status=?" + index++, pageQuery.status);
        }

        if (pageQuery.tags != null && !pageQuery.tags.isEmpty()) {
            query.append("AND (");
            for (int i = 0; i < pageQuery.tags.size(); i++) {
                if (i != 0) {
                    query.append("OR");
                }
                query.append("t.tags LIKE ?" + index++, "%" + pageQuery.tags.get(i) + "%");
            }
            query.append(")");
        }

        if (pageQuery.startTime != null) {
            query.append("AND t.createdTime>=?" + index++, pageQuery.startTime);
        }

        if (pageQuery.endTime != null) {
            query.append("AND t.createdTime<?" + index, pageQuery.endTime);
        }

        query.limit(pageQuery.page, pageQuery.limit);
        if (!Strings.isNullOrEmpty(pageQuery.sortingField)) {
            query.sort("t." + pageQuery.sortingField, pageQuery.desc);
        }
        return query.findAll();
    }

    public Optional<Page> findById(String id) {
        return repository.query("SELECT t from Page t WHERE t.id=?0", id).findOne();
    }

    public Optional<Page> findByPath(String path) {
        return repository.query("SELECT t from Page t WHERE t.path=?0 AND t.status=?1", path, PageStatus.ACTIVE).findOne();
    }


    public Optional<Page> findNext(String id) {
        Page page = get(id);
        return repository.query("SELECT t from Page t WHERE t.updatedTime>?0 AND t.categoryId!=null", page.updatedTime).sort("t.updatedTime", false).findOne();
    }

    public Optional<Page> findPrev(String id) {
        Page page = get(id);
        return repository.query("SELECT t from Page t WHERE t.updatedTime<?0 AND t.categoryId!=null", page.updatedTime).sort("t.updatedTime", true).findOne();
    }

    public List<Page> findRelated(PageRelatedQuery pageRelatedQuery) {
        Optional<Page> pageOptional = findById(pageRelatedQuery.id);
        if (!pageOptional.isPresent()) {
            return ImmutableList.of();
        }
        Page page = pageOptional.get();
        int index = 0;
        Query<Page> query = repository.query("SELECT t from Page t WHERE t.id!=?" + index++, page.id);

        if (page.categoryId != null) {
            query.append("AND t.categoryId=?" + index++, page.categoryId);
        }
        if (!Strings.isNullOrEmpty(page.tags)) {
            List<String> tags = Splitter.on(";").splitToList(page.tags);
            query.append("AND (");
            for (int i = 0; i < tags.size(); i++) {
                if (i != 0) {
                    query.append("OR");
                }
                query.append("t.tags LIKE ?" + index++, "%" + tags.get(i) + "%");
            }
            query.append(")");
        }
        return query.limit(1, pageRelatedQuery.limit).find();
    }

    @Transactional
    public void delete(String id, String requestBy) {
        Page page = repository.get(id);
        if (page.status == PageStatus.INACTIVE) {
            pageContentService.delete(page.id);
            repository.delete(page.id);
            pageDraftService.deleteByPageId(page.id);
            page.status = PageStatus.DELETED;

            notifyPageDelete(page);
        } else {
            page.status = PageStatus.INACTIVE;
            page.updatedBy = requestBy;
            page.updatedTime = OffsetDateTime.now();
            repository.update(id, page);
        }
    }

    private void notifyPageChanged(Page page, boolean firstPublished) {
        PageChangedMessage pageChangedMessage = new PageChangedMessage();
        pageChangedMessage.id = page.id;
        pageChangedMessage.firstPublished = firstPublished;
        pageChangedMessage.path = page.path;
        pageChangedMessage.templatePath = page.templatePath;
        pageChangedMessage.userId = page.userId;
        pageChangedMessage.title = page.title;
        pageChangedMessage.categoryId = page.categoryId;
        pageChangedMessage.tags = page.tags == null ? ImmutableList.of() : Splitter.on(";").splitToList(page.tags);
        pageChangedMessage.keywords = page.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(page.keywords);
        pageChangedMessage.fields = page.fields == null ? ImmutableMap.of() : JSON.fromJSON(page.fields, Map.class);
        pageChangedMessage.status = page.status;
        pageChangedMessage.description = page.description;
        pageChangedMessage.updatedBy = page.updatedBy;
        pageChangedMessage.updatedTime = page.updatedTime;
        pageChangedMessage.createdBy = page.createdBy;
        pageChangedMessage.createdTime = page.createdTime;
        pageChangedMessagePublisher.publish(pageChangedMessage);
    }

    private void notifyPageDelete(Page page) {
        PageDeletedMessage pageChangedMessage = new PageDeletedMessage();
        pageChangedMessage.id = page.id;
        pageChangedMessage.path = page.path;
        pageChangedMessage.templatePath = page.templatePath;
        pageChangedMessage.userId = page.userId;
        pageChangedMessage.categoryId = page.categoryId;
        pageChangedMessage.status = page.status;
        pageChangedMessage.description = page.description;
        pageChangedMessage.updatedBy = page.updatedBy;
        pageChangedMessage.updatedTime = page.updatedTime;
        pageChangedMessage.createdBy = page.createdBy;
        pageChangedMessage.createdTime = page.createdTime;
        pageDeletePublisher.publish(pageChangedMessage);
    }

    @Transactional
    public Page publish(String id, String requestBy) {
        PageDraft draft = pageDraftService.get(id);
        if (!Strings.isNullOrEmpty(draft.pageId)) {
            Page page = repository.get(draft.pageId);
            page.path = draft.path;
            page.templatePath = draft.templatePath;
            page.version = draft.version == null ? Integer.valueOf(1) : draft.version;
            page.categoryId = draft.categoryId;
            page.userId = draft.userId;
            page.title = draft.title;
            page.description = draft.description;
            page.tags = draft.tags;
            page.keywords = draft.keywords;
            page.fields = draft.fields;
            page.imageURL = draft.imageURL;
            page.imageURLs = draft.imageURLs;
            page.status = PageStatus.ACTIVE;
            page.updatedTime = OffsetDateTime.now();
            page.updatedBy = requestBy;
            pageContentService.update(page.id, draft.content, requestBy);
            repository.update(page.id, page);
            pageDraftService.delete(id);
            notifyPageChanged(page, false);
            return page;
        } else {
            Page page = new Page();
            page.id = UUID.randomUUID().toString();
            page.userId = draft.userId;
            page.path = draft.path;
            page.templatePath = draft.templatePath;
            page.fields = draft.fields;
            page.version = 1;
            page.categoryId = draft.categoryId;
            page.title = draft.title;
            page.description = draft.description;
            page.tags = draft.tags;
            page.keywords = draft.keywords;
            page.imageURL = draft.imageURL;
            page.imageURLs = draft.imageURLs;
            page.status = PageStatus.ACTIVE;
            page.createdTime = OffsetDateTime.now();
            page.createdBy = requestBy;
            page.updatedTime = OffsetDateTime.now();
            page.updatedBy = requestBy;
            pageContentService.create(page.id, draft.content, requestBy);
            repository.insert(page);
            pageDraftService.delete(id);
            notifyPageChanged(page, true);
            return page;
        }
    }

    @Transactional
    public void revert(String id, String requestBy) {
        Page page = get(id);
        if (page.status != PageStatus.INACTIVE) {
            throw Exceptions.badRequestException("status", "page is not inactive");
        }
        page.status = PageStatus.ACTIVE;
        page.updatedBy = requestBy;
        page.updatedTime = OffsetDateTime.now();
        repository.update(page.id, page);
        notifyPageChanged(page, false);
    }


    public List<Page> latest(LatestPageQuery query) {
        return repository.query("SELECT t FROM Page t WHERE t.status=?0 ORDER BY t.createdTime DESC ", PageStatus.ACTIVE).limit(1, query.limit).find();
    }

    @Transactional
    public Page create(CreatePageRequest request) {
        Optional<Page> pageOptional = findByPath(request.path);
        if (pageOptional.isPresent()) {
            Page page = pageOptional.get();
            page.path = URLs.normalize(request.path);
            page.templatePath = request.templatePath;
            page.version = page.version + 1;
            page.categoryId = request.categoryId;
            page.userId = request.userId;
            page.title = request.title;
            page.description = request.description;
            page.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
            page.keywords = request.keywords == null ? null : Joiner.on(';').join(request.keywords);
            page.fields = request.fields == null ? null : JSON.toJSON(request.fields);
            page.imageURL = request.imageURL;
            page.imageURLs = Joiner.on(";").join(Markdown.imageURLs(request.content));
            page.status = PageStatus.ACTIVE;
            page.updatedTime = OffsetDateTime.now();
            page.updatedBy = request.requestBy;
            pageContentService.update(page.id, request.content, request.requestBy);
            repository.update(page.id, page);
            notifyPageChanged(page, true);
            return page;
        } else {
            Page page = new Page();
            page.id = UUID.randomUUID().toString();
            page.userId = request.userId;
            page.path = URLs.normalize(request.path);
            page.templatePath = request.templatePath;
            page.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
            page.keywords = request.keywords == null ? null : Joiner.on(';').join(request.keywords);
            page.fields = request.fields == null ? null : JSON.toJSON(request.fields);
            page.version = 1;
            page.categoryId = request.categoryId;
            page.title = request.title;
            page.description = request.description;
            page.imageURL = request.imageURL;
            page.imageURLs = Joiner.on(";").join(Markdown.imageURLs(request.content));
            page.status = PageStatus.ACTIVE;
            page.createdTime = OffsetDateTime.now();
            page.createdBy = request.requestBy;
            page.updatedTime = OffsetDateTime.now();
            page.updatedBy = request.requestBy;
            pageContentService.create(page.id, request.content, request.requestBy);
            repository.insert(page);
            notifyPageChanged(page, false);
            return page;
        }
    }
}