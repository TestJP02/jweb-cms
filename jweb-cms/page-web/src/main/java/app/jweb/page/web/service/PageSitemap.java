package app.jweb.page.web.service;

import app.jweb.page.api.PageWebService;
import app.jweb.page.api.page.PageQuery;
import app.jweb.page.api.page.PageResponse;
import app.jweb.util.collection.QueryResponse;
import app.jweb.web.Sitemap;
import app.jweb.web.WebLink;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * @author chi
 */
public class PageSitemap implements Sitemap {
    @Inject
    PageWebService postWebService;

    @Override
    public long totalLinks() {
        PageQuery postQuery = new PageQuery();
        postQuery.page = 1;
        postQuery.limit = 0;
        return postWebService.find(postQuery).total;
    }

    @Override
    public Iterator<WebLink> iterator() {
        return new PageIterator(postWebService);
    }

    static class PageIterator implements Iterator<WebLink> {
        private final PageWebService postWebService;
        Iterator<PageResponse> iterator;
        private int page = 1;

        PageIterator(PageWebService postWebService) {
            this.postWebService = postWebService;
        }

        @Override
        public boolean hasNext() {
            if (iterator == null || !iterator.hasNext()) {
                PageQuery postQuery = new PageQuery();
                postQuery.page = page;
                postQuery.limit = 1000;
                QueryResponse<PageResponse> posts = postWebService.find(postQuery);
                iterator = posts.items.iterator();
                page++;
            }
            return iterator.hasNext();
        }

        @Override
        public WebLink next() {
            PageResponse post = iterator.next();
            WebLink webLink = new WebLink();
            webLink.path = post.path;
            webLink.updatedTime = post.updatedTime;
            return webLink;
        }
    }
}
