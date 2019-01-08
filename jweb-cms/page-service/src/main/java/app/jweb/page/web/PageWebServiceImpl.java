package app.jweb.page.web;

import app.jweb.page.api.PageWebService;
import app.jweb.page.api.page.CreatePageRequest;
import app.jweb.page.api.page.DeletePageRequest;
import app.jweb.page.api.page.PageQuery;
import app.jweb.page.api.page.PageResponse;
import app.jweb.page.api.page.UpdatePageRequest;
import app.jweb.page.domain.Page;
import app.jweb.page.service.PageComponentService;
import app.jweb.page.service.PageSavedComponentService;
import app.jweb.page.service.PageService;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class PageWebServiceImpl implements PageWebService {
    @Inject
    PageService pageService;
    @Inject
    PageSavedComponentService pageSavedComponentService;
    @Inject
    PageComponentService pageComponentService;

    @Override
    public PageResponse get(String id) {
        return response(pageService.get(id));
    }

    @Override
    public Optional<PageResponse> findByPath(String path) {
        Optional<Page> pageTemplate = pageService.findByPath(path);
        return pageTemplate.map(this::response);
    }

    @Override
    public QueryResponse<PageResponse> find(PageQuery query) {
        return pageService.find(query).map(this::response);
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
    public void delete(DeletePageRequest request) {
        request.ids.forEach(id -> pageService.delete(id, request.requestBy));
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
        response.status = page.status;
        response.createdTime = page.createdTime;
        response.createdBy = page.createdBy;
        response.updatedTime = page.updatedTime;
        response.updatedBy = page.updatedBy;
        return response;
    }

}
