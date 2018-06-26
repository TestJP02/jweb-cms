package io.sited.page.service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.page.api.draft.CreateDraftRequest;
import io.sited.page.api.draft.DraftQuery;
import io.sited.page.api.draft.UpdateDraftRequest;
import io.sited.page.api.page.PageStatus;
import io.sited.page.domain.PageDraft;
import io.sited.page.util.Markdown;
import io.sited.page.util.URLs;
import io.sited.util.JSON;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PageDraftService {
    @Inject
    Repository<PageDraft> repository;
    @Inject
    PageCategoryService pageCategoryService;

    public PageDraft get(String id) {
        return repository.get(id);
    }

    public Optional<PageDraft> findByPath(String path) {
        return repository.query("SELECT t FROM PageDraft t WHERE t.path=?0", path).findOne();
    }

    public Optional<PageDraft> findByPageId(String pageId) {
        return repository.query("SELECT t FROM PageDraft t WHERE t.pageId=?0", pageId).findOne();
    }

    public QueryResponse<PageDraft> find(DraftQuery draftQuery) {
        int index = 0;
        Query<PageDraft> query = repository.query("SELECT t FROM PageDraft t WHERE 1=1");
        if (!Strings.isNullOrEmpty(draftQuery.query)) {
            query.append("AND (t.title LIKE ?" + index++ + " OR t.path LIKE ?" + index++ + ")", "%" + draftQuery.query + "%", "%" + draftQuery.query + "%");
        }
        if (!Strings.isNullOrEmpty(draftQuery.categoryId)) {
            List<String> ids = pageCategoryService.childrenIds(draftQuery.categoryId);
            query.append("AND t.categoryId IN (");
            for (int i = 0; i < ids.size(); i++) {
                if (i != 0) query.append(",");
                String categoryId = ids.get(i);
                query.append("?" + index++, categoryId);
                index++;
            }
            query.append(")");
        }

        if (draftQuery.status != null) {
            query.append("AND t.status=?" + index, draftQuery.status);
        }

        query.limit(draftQuery.page, draftQuery.limit);
        if (!Strings.isNullOrEmpty(draftQuery.sortingField)) {
            query.sort("t." + draftQuery.sortingField, draftQuery.desc);
        }
        return query.findAll();
    }

    @Transactional
    public PageDraft update(String id, UpdateDraftRequest request) {
        PageDraft draft = get(id);
        draft.categoryId = request.categoryId;
        draft.path = URLs.normalize(request.path);
        draft.templatePath = request.templatePath;
        draft.title = request.title;
        draft.description = request.description;
        draft.content = request.content;
        draft.tags = request.tags == null || request.tags.isEmpty() ? null : Joiner.on(';').join(request.tags);
        draft.keywords = request.keywords == null || request.keywords.isEmpty() ? null : Joiner.on(';').join(request.keywords);
        draft.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        draft.imageURL = request.imageURL;
        draft.updatedTime = OffsetDateTime.now();
        draft.updatedBy = request.requestBy;
        repository.update(draft.id, draft);
        return draft;
    }

    @Transactional
    public PageDraft create(CreateDraftRequest request) {
        PageDraft draft = new PageDraft();
        draft.id = UUID.randomUUID().toString();
        draft.pageId = request.pageId;
        draft.categoryId = request.categoryId;
        draft.version = request.version;
        draft.path = URLs.normalize(request.path);
        draft.templatePath = request.templatePath;
        draft.title = request.title;
        draft.description = request.description;
        draft.tags = request.tags == null || request.tags.isEmpty() ? null : Joiner.on(';').join(request.tags);
        draft.keywords = request.keywords == null || request.keywords.isEmpty() ? null : Joiner.on(';').join(request.keywords);
        draft.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        draft.imageURL = request.imageURL;
        draft.imageURLs = Joiner.on(";").join(Markdown.imageURLs(request.content));
        draft.userId = request.userId;
        draft.updatedBy = request.requestBy;
        draft.updatedTime = OffsetDateTime.now();
        draft.createdBy = request.requestBy;
        draft.createdTime = OffsetDateTime.now();
        draft.status = PageStatus.DRAFT;
        draft.content = request.content;
        repository.insert(draft);
        return draft;
    }


    @Transactional
    public void create(PageDraft pageDraft) {
        repository.insert(pageDraft);
    }

    @Transactional
    public void delete(String id) {
        repository.delete(id);
    }

    @Transactional
    public void deleteByPageId(String pageId) {
        repository.execute("DELETE FROM PageDraft t WHERE t.pageId=?", pageId);
    }
}
