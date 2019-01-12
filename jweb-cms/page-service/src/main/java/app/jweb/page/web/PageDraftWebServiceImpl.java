package app.jweb.page.web;

import app.jweb.page.api.PageDraftWebService;
import app.jweb.page.api.page.CreatePageRequest;
import app.jweb.page.api.page.GetPageDraftRequest;
import app.jweb.page.api.page.PageDraftQuery;
import app.jweb.page.api.page.PageResponse;
import app.jweb.page.api.page.PageStatus;
import app.jweb.page.api.page.PublishPageRequest;
import app.jweb.page.api.page.UpdatePageRequest;
import app.jweb.page.domain.Page;
import app.jweb.page.domain.PageDraft;
import app.jweb.page.service.PageDraftService;
import app.jweb.page.service.PageService;
import app.jweb.page.service.PageTemplateService;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class PageDraftWebServiceImpl implements PageDraftWebService {
    @Inject
    PageDraftService pageDraftService;

    @Inject
    PageService pageService;
    @Inject
    PageTemplateService pageTemplateService;

    @Override
    public QueryResponse<PageResponse> find(PageDraftQuery query) {
        return null;
    }

    @Override
    public Optional<PageResponse> findByPath(String path) {
        return Optional.empty();
    }

    @Override
    public PageResponse get(GetPageDraftRequest request) {
        Optional<PageDraft> pageDraftOptional = pageDraftService.findByPageId(request.pageId);
        if (pageDraftOptional.isPresent()) {
            Optional<Page> page = pageService.findById(pageDraftOptional.get().draftId);
            if (page.isPresent()) {
                return response(page.get());
            }
        }
        Page page = pageService.get(request.pageId);
        if (page.status == PageStatus.DRAFT) {
            return response(page);
        } else {
            Page draft = pageService.copy(page);
            pageTemplateService.copySections(page.id, draft.id, request.requestBy);
            pageDraftService.create(draft.id, page.id, request.requestBy);
            return response(draft);
        }
    }

    @Override
    public PageResponse create(CreatePageRequest request) {
        return response(pageService.create(request));
    }

    @Override
    public PageResponse update(String id, UpdatePageRequest request) {
        return response(pageService.update(id, request));
    }

    @Override
    public PageResponse publish(PublishPageRequest request) {
        Page page = pageService.publish(request);
        return response(page);
    }

    private PageResponse response(Page page) {
        PageResponse response = new PageResponse();
        response.id = page.id;
        response.userId = page.userId;
        response.categoryId = page.categoryId;
        response.categoryIds = page.categoryIds == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.categoryIds);
        response.path = page.path;
        response.title = page.title;
        response.description = page.description;
        response.tags = page.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.tags);
        response.keywords = page.keywords == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.keywords);
        response.status = page.status;
        response.createdTime = page.createdTime;
        response.createdBy = page.createdBy;
        response.updatedTime = page.updatedTime;
        response.updatedBy = page.updatedBy;
        return response;
    }
}
