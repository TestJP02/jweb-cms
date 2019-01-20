package app.jweb.page.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.message.MessagePublisher;
import app.jweb.page.api.page.CreatePageRequest;
import app.jweb.page.api.page.PageCreatedMessage;
import app.jweb.page.api.page.PageDeletedMessage;
import app.jweb.page.api.page.PageDraftQuery;
import app.jweb.page.api.page.PageQuery;
import app.jweb.page.api.page.PageStatus;
import app.jweb.page.api.page.PageUpdatedMessage;
import app.jweb.page.api.page.PublishPageRequest;
import app.jweb.page.api.page.UpdatePageRequest;
import app.jweb.page.api.page.ValidatePagePathRequest;
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
import javax.ws.rs.BadRequestException;
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
        return repository.query("SELECT t FROM Page t WHERE t.path=?0 AND t.status=?1", path, PageStatus.ACTIVE).findOne();
    }

    public Optional<Page> findDraftByPath(String path) {
        return repository.query("SELECT t FROM Page t WHERE t.path=?0 AND t.status=?1", path, PageStatus.DRAFT).findOne();
    }

    public Optional<Page> findById(String id) {
        return repository.query("SELECT t FROM Page t WHERE t.id=?0", id).findOne();
    }

    public QueryResponse<Page> find(PageQuery pageQuery) {
        Query<Page> query = repository.query("SELECT t FROM Page t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(pageQuery.query)) {
            query.append("AND (t.path LIKE ?" + index++, '%' + pageQuery.query + "%");
            query.append("OR t.title LIKE ?" + (index++) + ')', '%' + pageQuery.query + "%");
        }
        query.append("AND t.status=?" + index, PageStatus.ACTIVE);
        query.limit(pageQuery.page, pageQuery.limit);
        if (pageQuery.sortingField != null) {
            query.sort("t." + pageQuery.sortingField, pageQuery.desc);
        } else {
            query.sort("t.updatedTime", true);
        }
        return query.findAll();
    }

    public QueryResponse<Page> find(PageDraftQuery pageQuery) {
        Query<Page> query = repository.query("SELECT t FROM Page t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(pageQuery.query)) {
            query.append("AND (t.path LIKE ?" + index++, '%' + pageQuery.query + "%");
            query.append("OR t.title LIKE ?" + (index++) + ')', '%' + pageQuery.query + "%");
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
        page.tags = request.tags == null ? null : Joiner.on(';').skipNulls().join(request.tags);
        page.keywords = request.keywords == null ? null : Joiner.on(';').skipNulls().join(request.keywords);
        page.createdTime = OffsetDateTime.now();
        page.updatedTime = OffsetDateTime.now();
        page.createdBy = request.requestBy;
        page.updatedBy = request.requestBy;
        page.status = PageStatus.DRAFT;
        repository.insert(page);
        pageTemplateService.create(page.id, request.sections, request.requestBy);
        return page;
    }

    @Transactional
    public Page copy(Page request) {
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
        page.tags = request.tags;
        page.keywords = request.keywords;
        page.createdTime = OffsetDateTime.now();
        page.updatedTime = OffsetDateTime.now();
        page.createdBy = request.createdBy;
        page.updatedBy = request.updatedBy;
        page.status = PageStatus.DRAFT;
        repository.insert(page);
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
        if (request.categoryId != null) {
            PageCategory category = categoryService.get(request.categoryId);
            page.categoryId = category.id;
            page.categoryIds = categoryIds(category);
        }
        page.path = request.path;
        page.title = request.title;
        page.description = request.description;
        page.tags = request.tags == null ? null : Joiner.on(';').skipNulls().join(request.tags);
        page.updatedTime = OffsetDateTime.now();
        page.updatedBy = request.requestBy;
        repository.update(id, page);
        pageTemplateService.update(page.id, request.sections, request.requestBy);
        return page;
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
    public Page publish(PublishPageRequest request) {
        Page draft = repository.get(request.draftId);
        Optional<PageDraft> draftOptional = pageDraftService.findByDraftId(request.draftId);
        if (draftOptional.isPresent()) {
            PageDraft pageDraft = draftOptional.get();
            Page page = get(pageDraft.pageId);

            if (pathExists(draft.path, page.id)) {
                throw new BadRequestException("duplicate path, path=" + draft.path);
            }

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
            pageTemplateService.replace(draft.id, page.id, request.requestBy);

            pageTemplateService.delete(draft.id);
            pageDraftService.deleteByPageId(draft.id);
            repository.delete(draft.id);

            notifyPageUpdated(page);
            return page;
        } else {
            if (pathExists(draft.path, draft.id)) {
                throw new BadRequestException("duplicate path, path=" + draft.path);
            }
            draft.status = PageStatus.ACTIVE;
            draft.updatedTime = OffsetDateTime.now();
            draft.updatedBy = request.requestBy;
            repository.update(draft.id, draft);
            notifyPageCreated(draft);
            return draft;
        }
    }

    public boolean validatePath(ValidatePagePathRequest request) {
        Optional<PageDraft> draftOptional = pageDraftService.findByDraftId(request.draftId);
        if (draftOptional.isPresent()) {
            return !pathExists(request.path, draftOptional.get().pageId);
        } else {
            return !pathExists(request.path, request.draftId);
        }
    }

    private boolean pathExists(String path, String pageId) {
        Optional<Page> pageOptional = findByPath(path);
        return pageOptional.isPresent() && !pageId.equals(pageOptional.get().id);
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
