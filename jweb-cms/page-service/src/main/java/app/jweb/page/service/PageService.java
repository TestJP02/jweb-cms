package app.jweb.page.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.message.MessagePublisher;
import app.jweb.page.api.page.CreatePageRequest;
import app.jweb.page.api.page.PageCreatedMessage;
import app.jweb.page.api.page.PageDeletedMessage;
import app.jweb.page.api.page.PageQuery;
import app.jweb.page.api.page.PageStatus;
import app.jweb.page.api.page.PageUpdatedMessage;
import app.jweb.page.api.page.PublishPageRequest;
import app.jweb.page.api.page.UpdatePageRequest;
import app.jweb.page.domain.Page;
import app.jweb.page.domain.PageCategory;
import app.jweb.page.domain.PageDraft;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PageService {
    @Inject
    Repository<Page> repository;
    @Inject
    MessagePublisher<PageCreatedMessage> createdMessageMessagePublisher;
    @Inject
    MessagePublisher<PageUpdatedMessage> updatedMessagePublisher;
    @Inject
    MessagePublisher<PageDeletedMessage> deletedMessageMessagePublisher;
    @Inject
    PageCategoryService categoryService;
    @Inject
    PageTemplateService pageTemplateService;
    @Inject
    PageDraftService pageDraftService;

    public Page get(String id) {
        return repository.get(id);
    }

    public Optional<Page> findByPath(String path) {
        return repository.query("SELECT t FROM Page t WHERE t.path=?0", path).findOne();
    }

    public QueryResponse<Page> find(PageQuery pageQuery) {
        Query<Page> query = repository.query("SELECT t FROM Page t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(pageQuery.query)) {
            query.append("AND (t.path LIKE ?" + index++, '%' + pageQuery.query + "%");
            query.append("OR t.title LIKE ?" + index++, '%' + pageQuery.query + "%)");
        }
        if (pageQuery.status != null) {
            query.append("AND t.status=?" + index, pageQuery.status);
        }
        query.limit(pageQuery.page, pageQuery.limit);
        if (pageQuery.sortingField != null) {
            query.sort("t." + pageQuery.sortingField, pageQuery.desc);
        } else {
            query.sort("t.updatedTime", true);
        }
        return query.findAll();
    }

    @Transactional
    public Page create(CreatePageRequest request) {
        Page page = new Page();
        page.id = UUID.randomUUID().toString();
        if (request.categoryId != null) {
            PageCategory category = categoryService.get(request.categoryId);
            page.categoryId = category.id;
            page.categoryIds = categoryIds(category);
        }
        page.userId = request.userId;
        page.path = request.path;
        page.title = request.title;
        page.description = request.description;
        page.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
        page.createdTime = OffsetDateTime.now();
        page.updatedTime = OffsetDateTime.now();
        page.createdBy = request.requestBy;
        page.updatedBy = request.requestBy;
        page.status = PageStatus.DRAFT;
        repository.insert(page);
        pageTemplateService.create(page.id, request.sections, request.requestBy);
        notifyPageCreated(page);
        return page;
    }

    private String categoryIds(PageCategory category) {
        if (category.parentIds == null) {
            return category.id;
        }
        return category.parentIds + ';' + category.id;
    }

    @Transactional
    public Page update(String id, UpdatePageRequest request) {
        Page page = get(id);
        if (page.status == PageStatus.ACTIVE) {
            Optional<PageDraft> draftOptional = pageDraftService.findByPageId(page.id);
            if (draftOptional.isEmpty()) {
                Page draft = new Page();
                draft.id = UUID.randomUUID().toString();
                if (request.categoryId != null) {
                    PageCategory category = categoryService.get(request.categoryId);
                    draft.categoryId = category.id;
                    draft.categoryIds = categoryIds(category);
                }
                draft.userId = page.userId;
                draft.path = request.path;
                draft.title = request.title;
                draft.description = request.description;
                draft.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
                draft.createdTime = OffsetDateTime.now();
                draft.updatedTime = OffsetDateTime.now();
                draft.createdBy = request.requestBy;
                draft.updatedBy = request.requestBy;
                draft.status = PageStatus.DRAFT;
                repository.insert(draft);
                pageTemplateService.create(draft.id, request.sections, request.requestBy);
                pageDraftService.create(draft.id, page.id, request.requestBy);
                return draft;
            } else {
                Page draft = get(draftOptional.get().draftId);
                if (request.categoryId != null) {
                    PageCategory category = categoryService.get(request.categoryId);
                    draft.categoryId = category.id;
                    draft.categoryIds = categoryIds(category);
                }
                draft.path = request.path;
                draft.title = request.title;
                draft.description = request.description;
                draft.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
                draft.createdTime = OffsetDateTime.now();
                draft.updatedTime = OffsetDateTime.now();
                draft.createdBy = request.requestBy;
                draft.updatedBy = request.requestBy;
                draft.status = PageStatus.DRAFT;
                repository.insert(draft);
                pageTemplateService.create(draft.id, request.sections, request.requestBy);
                return draft;
            }
        } else {
            if (request.categoryId != null) {
                PageCategory category = categoryService.get(request.categoryId);
                page.categoryId = category.id;
                page.categoryIds = categoryIds(category);
            }
            page.path = request.path;
            page.title = request.title;
            page.description = request.description;
            page.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
            page.updatedTime = OffsetDateTime.now();
            page.updatedBy = request.requestBy;
            repository.update(id, page);
            pageTemplateService.update(page.id, request.sections, request.requestBy);
            return page;
        }
    }


    @Transactional
    public void delete(String id) {
        Page page = repository.get(id);
        repository.delete(id);
        pageTemplateService.delete(page.id);
        pageDraftService.deleteByPageId(page.id);
        notifyPageDeleted(page);
    }

    @Transactional
    public void publish(PublishPageRequest request) {
        Page draft = repository.get(request.draftId);
        Optional<PageDraft> draftOptional = pageDraftService.findByDraftId(request.draftId);
        if (draftOptional.isPresent()) {
            PageDraft pageDraft = draftOptional.get();
            Page page = get(pageDraft.pageId);
            if (draft.categoryId != null) {
                PageCategory category = categoryService.get(draft.categoryId);
                draft.categoryId = category.id;
                draft.categoryIds = categoryIds(category);
            }
            page.path = draft.path;
            page.title = draft.title;
            page.description = draft.description;
            page.tags = draft.tags;
            page.keywords = draft.keywords;
            page.updatedTime = OffsetDateTime.now();
            page.updatedBy = request.requestBy;
            repository.update(page.id, page);
            pageTemplateService.copySections(draft.id, page.id, request.requestBy);

            pageTemplateService.delete(draft.id);
            repository.delete(draft.id);

            notifyPageUpdated(page);
        } else {
            draft.status = PageStatus.ACTIVE;
            draft.updatedTime = OffsetDateTime.now();
            draft.updatedBy = request.requestBy;
            repository.update(draft.id, draft);
            notifyPageCreated(draft);
        }
    }

    private void notifyPageCreated(Page page) {
        PageCreatedMessage message = new PageCreatedMessage();
        message.id = page.id;
        message.userId = page.userId;
        message.categoryId = page.categoryId;
        message.categoryIds = page.categoryIds == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.categoryIds);
        message.path = page.path;
        message.title = page.title;
        message.description = page.description;
        message.tags = page.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.tags);
        message.status = page.status;
        message.createdTime = page.createdTime;
        message.createdBy = page.createdBy;
        message.updatedTime = page.updatedTime;
        message.updatedBy = page.updatedBy;
        createdMessageMessagePublisher.publish(message);
    }

    private void notifyPageUpdated(Page page) {
        PageUpdatedMessage message = new PageUpdatedMessage();
        message.id = page.id;
        message.userId = page.userId;
        message.categoryId = page.categoryId;
        message.categoryIds = page.categoryIds == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.categoryIds);
        message.path = page.path;
        message.title = page.title;
        message.description = page.description;
        message.tags = page.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.tags);
        message.status = page.status;
        message.createdTime = page.createdTime;
        message.createdBy = page.createdBy;
        message.updatedTime = page.updatedTime;
        message.updatedBy = page.updatedBy;
        updatedMessagePublisher.publish(message);
    }

    private void notifyPageDeleted(Page page) {
        PageDeletedMessage message = new PageDeletedMessage();
        message.id = page.id;
        message.userId = page.userId;
        message.categoryId = page.categoryId;
        message.categoryIds = page.categoryIds == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.categoryIds);
        message.path = page.path;
        message.title = page.title;
        message.description = page.description;
        message.tags = page.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.tags);
        message.status = page.status;
        message.createdTime = page.createdTime;
        message.createdBy = page.createdBy;
        message.updatedTime = page.updatedTime;
        message.updatedBy = page.updatedBy;
        deletedMessageMessagePublisher.publish(message);
    }
}
