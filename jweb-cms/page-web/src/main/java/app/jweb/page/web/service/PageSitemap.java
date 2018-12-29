package app.jweb.page.web.service;

import app.jweb.post.api.PostWebService;
import app.jweb.post.api.post.PostQuery;
import app.jweb.post.api.post.PostResponse;
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
    PostWebService postWebService;

    @Override
    public long totalLinks() {
        PostQuery postQuery = new PostQuery();
        postQuery.page = 1;
        postQuery.limit = 0;
        return postWebService.find(postQuery).total;
    }

    @Override
    public Iterator<WebLink> iterator() {
        return new PageIterator(postWebService);
    }

    static class PageIterator implements Iterator<WebLink> {
        private final PostWebService postWebService;
        Iterator<PostResponse> iterator;
        private int page = 1;

        PageIterator(PostWebService postWebService) {
            this.postWebService = postWebService;
        }

        @Override
        public boolean hasNext() {
            if (iterator == null || !iterator.hasNext()) {
                PostQuery postQuery = new PostQuery();
                postQuery.page = page;
                postQuery.limit = 1000;
                QueryResponse<PostResponse> posts = postWebService.find(postQuery);
                iterator = posts.items.iterator();
                page++;
            }
            return iterator.hasNext();
        }

        @Override
        public WebLink next() {
            PostResponse post = iterator.next();
            WebLink webLink = new WebLink();
            webLink.path = post.path;
            webLink.updatedTime = post.updatedTime;
            return webLink;
        }
    }
}
