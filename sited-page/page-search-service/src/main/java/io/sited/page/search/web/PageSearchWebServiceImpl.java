package io.sited.page.search.web;

import io.sited.page.api.page.PageResponse;
import io.sited.page.search.api.PageSearchWebService;
import io.sited.page.search.api.page.SearchPageRequest;
import io.sited.page.search.service.PageSearchService;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageSearchWebServiceImpl implements PageSearchWebService {
    @Inject
    PageSearchService pageSearchService;

    @Override
    public QueryResponse<PageResponse> search(SearchPageRequest request) {
        return pageSearchService.search(request);
    }

    @Override
    public void fullIndex() {
        pageSearchService.fullIndex();
    }
}
