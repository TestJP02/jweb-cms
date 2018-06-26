package io.sited.page.web;


import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.sited.page.api.PageWebService;
import io.sited.page.api.page.CreatePageRequest;
import io.sited.page.api.page.DeletePageRequest;
import io.sited.page.api.page.LatestQuery;
import io.sited.page.api.page.PageNavigationResponse;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageRelatedQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageStatus;
import io.sited.page.api.page.RevertDeletePageRequest;
import io.sited.page.domain.Page;
import io.sited.page.service.PageDraftService;
import io.sited.page.service.PageService;
import io.sited.util.JSON;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageWebServiceImpl implements PageWebService {
    @Inject
    PageService pageService;
    @Inject
    PageDraftService pageDraftService;

    @Override
    public PageResponse get(String id) {
        return response(pageService.get(id));
    }

    @Override
    public List<PageResponse> batchGet(List<String> ids) {
        return pageService.batchGet(ids).stream().map(this::response).collect(Collectors.toList());
    }


    @Override
    public Optional<PageResponse> findByPath(String path) {
        return pageService.findByPath(path).map(this::response);
    }

    @Override
    public QueryResponse<PageResponse> find(PageQuery query) {
        return pageService.find(query).map(this::response);
    }

    @Override
    public List<PageResponse> findRelated(PageRelatedQuery query) {
        List<Page> pages = pageService.findRelated(query);
        return pages.stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public PageNavigationResponse navigation(String id) {
        PageNavigationResponse pageNavigationResponse = new PageNavigationResponse();
        pageNavigationResponse.next = pageService.findNext(id).map(this::response).orElse(null);
        pageNavigationResponse.prev = pageService.findPrev(id).map(this::response).orElse(null);
        return pageNavigationResponse;
    }

    @Override
    public PageResponse create(CreatePageRequest request) {
        return response(pageService.create(request));
    }


    @Override
    public void batchDelete(DeletePageRequest request) {
        if (request.pages == null) {
            return;
        }

        for (DeletePageRequest.PageView page : request.pages) {
            if (page.status == PageStatus.DRAFT || page.status == PageStatus.AUDITING) {
                pageDraftService.delete(page.id);
            } else {
                pageService.delete(page.id, request.requestBy);
            }
        }
    }

    @Override
    public void revertDelete(RevertDeletePageRequest request) {
        request.ids.forEach(id -> pageService.revert(id, request.requestBy));
    }

    @Override
    public List<PageResponse> latest(LatestQuery query) {
        return pageService.latest(query).stream().map(this::response).collect(Collectors.toList());
    }

    private PageResponse response(Page page) {
        PageResponse response = new PageResponse();
        response.id = page.id;
        response.userId = page.userId;
        response.categoryId = page.categoryId;
        response.tags = page.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(page.tags);
        response.keywords = page.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(page.keywords);
        response.fields = page.fields == null ? ImmutableMap.of() : JSON.fromJSON(page.fields, Map.class);
        response.imageURLs = page.imageURLs == null ? ImmutableList.of() : Splitter.on(";").splitToList(page.imageURLs);
        response.path = page.path;
        response.templatePath = page.templatePath;
        response.title = page.title;
        response.description = page.description;
        response.imageURL = page.imageURL;
        response.version = page.version;
        response.status = page.status;
        response.totalVisited = page.totalVisited;
        response.totalCommented = page.totalCommented;
        response.totalDisliked = page.totalDisliked;
        response.totalLiked = page.totalLiked;
        response.createdTime = page.createdTime;
        response.createdBy = page.createdBy;
        response.updatedTime = page.updatedTime;
        response.updatedBy = page.updatedBy;
        return response;
    }
}
