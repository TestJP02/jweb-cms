package app.jweb.page.search.web;

import app.jweb.page.search.api.SearchHistoryWebService;
import app.jweb.page.search.api.history.BatchDeleteSearchHistoryRequest;
import app.jweb.page.search.api.history.CreateSearchHistoryRequest;
import app.jweb.page.search.api.history.SearchHistoryQuery;
import app.jweb.page.search.api.history.SearchHistoryResponse;
import app.jweb.page.search.domain.SearchHistory;
import app.jweb.page.search.service.SearchHistoryService;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;

/**
 * @author chi
 */
public class SearchHistoryWebServiceImpl implements SearchHistoryWebService {
    @Inject
    SearchHistoryService searchHistoryService;

    @Override
    public QueryResponse<SearchHistoryResponse> find(SearchHistoryQuery query) {
        QueryResponse<SearchHistory> queryResponse = searchHistoryService.find(query);
        return queryResponse.map(this::response);
    }

    @Override
    public SearchHistoryResponse create(CreateSearchHistoryRequest request) {
        return response(searchHistoryService.create(request));
    }

    @Override
    public void batchDelete(BatchDeleteSearchHistoryRequest request) {
        searchHistoryService.batchDelete(request);
    }

    private SearchHistoryResponse response(SearchHistory searchHistory) {
        SearchHistoryResponse response = new SearchHistoryResponse();
        response.id = searchHistory.id;
        response.categoryId = searchHistory.categoryId;
        response.userId = searchHistory.userId;
        response.keywords = searchHistory.keywords;
        response.ip = searchHistory.ip;
        response.createdTime = searchHistory.createdTime;
        response.createdBy = searchHistory.createdBy;
        return response;
    }
}
