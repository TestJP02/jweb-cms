package io.sited.page.web.service;

import io.sited.cache.Cache;
import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageRelatedQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.util.collection.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class CachedPageService {
    private final Logger logger = LoggerFactory.getLogger(CachedPageService.class);

    @Inject
    Cache<PageResponse> cache;

    @Inject
    PageWebService pageWebService;

    public Optional<PageResponse> find(String path) {
        String cacheKey = cacheKey(path);
        Optional<PageResponse> cached = cache.get(cacheKey);
        if (cached.isPresent()) {
            return cached;
        }
        Optional<PageResponse> page = pageWebService.findByPath(path);
        page.ifPresent(pageVariableResponse -> cache.put(cacheKey, pageVariableResponse));
        return page;
    }

    public QueryResponse<PageResponse> find(PageQuery pageQuery) {
        return pageWebService.find(pageQuery);
    }

    public void invalidate(String path) {
        logger.info("reload page, path={}", path);
        String cacheKey = cacheKey(path);
        cache.delete(cacheKey);
    }

    private String cacheKey(String path) {
        return path;
    }

    public List<PageResponse> findRelated(PageRelatedQuery pageQuery) {
        return pageWebService.findRelated(pageQuery);
    }

    public void reload(String path) {
        String cacheKey = cacheKey(path);
        cache.delete(cacheKey);
    }
}
