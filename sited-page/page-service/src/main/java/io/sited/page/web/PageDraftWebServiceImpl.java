package io.sited.page.web;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.draft.BatchDeleteDraftRequest;
import io.sited.page.api.draft.CreateDraftRequest;
import io.sited.page.api.draft.DraftQuery;
import io.sited.page.api.draft.DraftResponse;
import io.sited.page.api.draft.UpdateDraftRequest;
import io.sited.page.api.page.PageResponse;
import io.sited.page.domain.Page;
import io.sited.page.domain.PageDraft;
import io.sited.page.service.PageDraftService;
import io.sited.page.service.PageService;
import io.sited.util.JSON;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class PageDraftWebServiceImpl implements PageDraftWebService {
    @Inject
    PageDraftService pageDraftService;
    @Inject
    PageService pageService;


    @Override
    public DraftResponse get(String id) {
        return response(pageDraftService.get(id));
    }

    @Override
    public QueryResponse<DraftResponse> find(DraftQuery query) {
        return pageDraftService.find(query).map(this::response);
    }

    @Override
    public Optional<DraftResponse> findByPageId(String pageId) {
        Optional<PageDraft> draft = pageDraftService.findByPageId(pageId);
        return draft.map(this::response);
    }

    @Override
    public Optional<DraftResponse> findByPath(String path) {
        return pageDraftService.findByPath(path).map(this::response);
    }

    @Override
    public DraftResponse create(CreateDraftRequest request) {
        PageDraft pageDraft = pageDraftService.create(request);
        return response(pageDraft);
    }

    @Override
    public DraftResponse update(String id, UpdateDraftRequest request) {
        PageDraft draft = pageDraftService.update(id, request);
        return response(draft);
    }

    @Override
    public PageResponse publish(String id, String requestBy) {
        return response(pageService.publish(id, requestBy));
    }

    @Override
    public void batchDelete(BatchDeleteDraftRequest request) {
        for (String id : request.ids) {
            pageDraftService.delete(id);
        }
    }

    private DraftResponse response(PageDraft draft) {
        DraftResponse response = new DraftResponse();
        response.id = draft.id;
        response.pageId = draft.pageId;
        response.userId = draft.userId;
        response.categoryId = draft.categoryId;
        response.path = draft.path;
        response.templatePath = draft.templatePath;
        response.tags = draft.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(draft.tags);
        response.keywords = draft.keywords == null ? ImmutableList.of() : Splitter.on(';').splitToList(draft.keywords);
        response.fields = draft.fields == null ? ImmutableMap.of() : JSON.fromJSON(draft.fields, Map.class);
        response.content = draft.content;
        response.title = draft.title;
        response.description = draft.description;
        response.imageURL = draft.imageURL;
        response.imageURLs = draft.imageURLs == null ? ImmutableList.of() : Splitter.on(";").splitToList(draft.imageURLs);
        response.userId = draft.userId;
        response.version = null;
        response.status = draft.status;
        response.createdTime = draft.createdTime;
        response.createdBy = draft.createdBy;
        response.updatedTime = draft.updatedTime;
        response.updatedBy = draft.updatedBy;
        return response;
    }


    private PageResponse response(Page page) {
        PageResponse response = new PageResponse();
        response.id = page.id;
        response.userId = page.userId;
        response.categoryId = page.categoryId;
        response.path = page.path;
        response.templatePath = page.templatePath;
        response.tags = page.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.tags);
        response.keywords = page.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(page.keywords);
        response.fields = page.fields == null ? ImmutableMap.of() : JSON.fromJSON(page.fields, Map.class);
        response.title = page.title;
        response.description = page.description;
        response.imageURL = page.imageURL;
        response.imageURLs = page.imageURLs == null ? ImmutableList.of() : Splitter.on(";").splitToList(page.imageURLs);
        response.version = page.version;
        response.status = page.status;
        response.createdTime = page.createdTime;
        response.createdBy = page.createdBy;
        response.updatedTime = page.updatedTime;
        response.updatedBy = page.updatedBy;
        return response;
    }
}
