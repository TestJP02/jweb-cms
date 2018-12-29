package app.jweb.page.search.web;

import app.jweb.page.search.api.PageSearchWebService;
import app.jweb.page.search.api.page.SearchPageQuery;
import app.jweb.page.search.service.PageSearchService;
import app.jweb.post.api.post.PostResponse;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageSearchWebServiceImpl implements PageSearchWebService {
    @Inject
    PageSearchService pageSearchService;

    @Override
    public QueryResponse<PostResponse> search(SearchPageQuery query) {
        return pageSearchService.search(query);
    }

    @Override
    public void fullIndex() {
        pageSearchService.fullIndex();
    }
}
