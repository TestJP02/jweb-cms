package app.jweb.page.web.service;

import app.jweb.post.api.PostCategoryWebService;
import app.jweb.post.api.category.CategoryQuery;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.util.collection.QueryResponse;
import app.jweb.web.Sitemap;
import app.jweb.web.WebLink;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * @author chi
 */
public class CategorySitemap implements Sitemap {
    @Inject
    PostCategoryWebService categoryWebService;

    @Override
    public long totalLinks() {
        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.page = 1;
        categoryQuery.limit = 0;
        return categoryWebService.find(categoryQuery).total;
    }

    @Override
    public Iterator<WebLink> iterator() {
        return new CategoryIterator(categoryWebService);
    }

    static class CategoryIterator implements Iterator<WebLink> {
        private final PostCategoryWebService categoryWebService;
        Iterator<CategoryResponse> iterator;
        private int page = 1;

        CategoryIterator(PostCategoryWebService categoryWebService) {
            this.categoryWebService = categoryWebService;
        }

        @Override
        public boolean hasNext() {
            if (iterator == null || !iterator.hasNext()) {
                CategoryQuery categoryQuery = new CategoryQuery();
                categoryQuery.page = page;
                categoryQuery.limit = 1000;
                QueryResponse<CategoryResponse> categories = categoryWebService.find(categoryQuery);
                iterator = categories.items.iterator();
                page++;
            }
            return iterator.hasNext();
        }

        @Override
        public WebLink next() {
            CategoryResponse category = iterator.next();
            WebLink webLink = new WebLink();
            webLink.path = category.path;
            webLink.updatedTime = category.updatedTime;
            return webLink;
        }
    }
}
