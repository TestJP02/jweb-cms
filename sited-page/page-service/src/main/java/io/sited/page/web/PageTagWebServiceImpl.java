package io.sited.page.web;

import io.sited.page.api.PageTagWebService;
import io.sited.page.api.tag.BatchDeletePageTagRequest;
import io.sited.page.api.tag.CreatePageTagRequest;
import io.sited.page.api.tag.PageTagQuery;
import io.sited.page.api.tag.PageTagResponse;
import io.sited.page.api.tag.UpdatePageTagRequest;
import io.sited.page.domain.PageTag;
import io.sited.page.service.PageTagService;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class PageTagWebServiceImpl implements PageTagWebService {
    @Inject
    PageTagService pageTagService;

    @Override
    public PageTagResponse get(String id) {
        return response(pageTagService.get(id));
    }

    @Override
    public Optional<PageTagResponse> findByName(String name) {
        return pageTagService.findByName(name).map(this::response);
    }

    @Override
    public QueryResponse<PageTagResponse> find(PageTagQuery query) {
        QueryResponse<PageTag> response = pageTagService.find(query);
        return response.map(this::response);
    }

    @Override
    public PageTagResponse create(CreatePageTagRequest request) {
        return response(pageTagService.create(request));
    }

    @Override
    public PageTagResponse update(String id, UpdatePageTagRequest request) {
        return response(pageTagService.update(id, request));
    }

    @Override
    public void batchDelete(BatchDeletePageTagRequest request) {
        pageTagService.batchDelete(request);
    }

    private PageTagResponse response(PageTag pageTag) {
        PageTagResponse response = new PageTagResponse();
        response.id = pageTag.id;
        response.name = pageTag.name;
        response.path = pageTag.path;
        response.totalTagged = pageTag.totalTagged;
        response.status = pageTag.status;
        response.createdTime = pageTag.createdTime;
        response.updatedTime = pageTag.updatedTime;
        response.createdBy = pageTag.createdBy;
        response.updatedBy = pageTag.updatedBy;
        return response;
    }
}
