package app.jweb.page.search.service;

import app.jweb.page.search.api.history.BatchDeleteSearchHistoryRequest;
import app.jweb.page.search.api.history.CreateSearchHistoryRequest;
import app.jweb.page.search.api.history.SearchHistoryQuery;
import app.jweb.page.search.domain.SearchHistory;
import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author chi
 */
public class SearchHistoryService {
    @Inject
    Repository<SearchHistory> repository;

    public QueryResponse<SearchHistory> find(SearchHistoryQuery query) {
        Query<SearchHistory> searchHistoryQuery = repository.query("SELECT t FROM SearchHistory t WHERE 1=1");
        int index = 0;
        if (query.keywords != null) {
            searchHistoryQuery.append("AND t.keywords LIKE ?" + index++, '%' + query.keywords + '%');
        }
        if (query.categoryId != null) {
            searchHistoryQuery.append("AND t.categoryId=?" + index, query.categoryId);
        }
        searchHistoryQuery.limit(query.page, query.limit);
        return searchHistoryQuery.findAll();
    }

    @Transactional
    public SearchHistory create(CreateSearchHistoryRequest request) {
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.id = UUID.randomUUID().toString();
        searchHistory.categoryId = request.categoryId;
        searchHistory.userId = request.userId;
        searchHistory.keywords = request.keywords;
        searchHistory.ip = request.ip;
        searchHistory.createdTime = OffsetDateTime.now();
        searchHistory.createdBy = request.requestBy;
        repository.insert(searchHistory);
        return searchHistory;
    }

    @Transactional
    public void batchDelete(BatchDeleteSearchHistoryRequest request) {
        repository.batchDelete(request.ids);
    }

}
