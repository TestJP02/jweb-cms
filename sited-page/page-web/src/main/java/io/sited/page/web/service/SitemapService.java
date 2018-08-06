package io.sited.page.web.service;

import io.sited.App;
import io.sited.ApplicationException;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.category.CategoryQuery;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.category.CategoryStatus;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageStatus;
import io.sited.resource.Resource;
import io.sited.util.collection.QueryResponse;
import io.sited.web.WebCache;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

/**
 * @author chi
 */
public class SitemapService {
    private static final int MAX_SITEMAP_ENTRIES = 5000;
    @Inject
    @Named("sitemap")
    WebCache webCache;
    @Inject
    PageWebService pageWebService;
    @Inject
    PageCategoryWebService pageCategoryWebService;
    @Inject
    App app;

    public SitemapService(WebCache webCache) {
        this.webCache = webCache;
    }

    public Resource index() {
        Optional<Resource> resource = webCache.get("sitemap.xml");
        if (resource.isPresent()) {
            return resource.get();
        }
        reload();
        return webCache.get("sitemap.xml").orElseThrow(() -> new ApplicationException("missing sitemap.xml"));
    }

    //TODO
    public void reload() {
        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.page = 1;
        categoryQuery.limit = MAX_SITEMAP_ENTRIES;
        categoryQuery.status = CategoryStatus.ACTIVE;
        QueryResponse<CategoryResponse> categories = pageCategoryWebService.find(categoryQuery);

        PageQuery pageQuery = new PageQuery();
        pageQuery.page = 1;
        pageQuery.limit = MAX_SITEMAP_ENTRIES;
        pageQuery.status = PageStatus.ACTIVE;
        QueryResponse<PageResponse> pages = pageWebService.find(pageQuery);

        SitemapBuilder builder = new SitemapBuilder(app.baseURL(), webCache, MAX_SITEMAP_ENTRIES);
        builder.appendCategories(categories.items);
        builder.appendPages(pages.items);
        while (categories.items.size() == MAX_SITEMAP_ENTRIES) {
            categoryQuery.page = categoryQuery.page + 1;
            categories = pageCategoryWebService.find(categoryQuery);
            builder.appendCategories(categories.items);
        }
        while (pages.items.size() == MAX_SITEMAP_ENTRIES) {
            pageQuery.page = pageQuery.page + 1;
            pages = pageWebService.find(pageQuery);
            builder.appendPages(pages.items);
        }
        builder.build();
    }

    public Optional<Resource> get(String path) {
        return webCache.get(path);
    }
}
