package io.sited.page.web.service;

import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageRelatedQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class PageService {
    @Inject
    PageWebService pageWebService;

    public Optional<PageResponse> find(String path) {
        return pageWebService.findByPath(path);
    }

    public QueryResponse<PageResponse> find(PageQuery pageQuery) {
        return pageWebService.find(pageQuery);
    }

    public List<PageResponse> findRelated(PageRelatedQuery pageQuery) {
        return pageWebService.findRelated(pageQuery);
    }
}
