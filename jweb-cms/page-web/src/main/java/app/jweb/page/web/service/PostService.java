package app.jweb.page.web.service;

import app.jweb.page.web.ContentEngine;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.post.PostQuery;
import app.jweb.post.api.post.PostRelatedQuery;
import app.jweb.post.api.post.PostResponse;
import app.jweb.util.collection.QueryResponse;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class PostService {
    private final Map<String, ContentEngine> engines = Maps.newHashMap();
    @Inject
    PostWebService postWebService;

    public Optional<PostResponse> find(String path) {
        return postWebService.findByPath(path);
    }

    public QueryResponse<PostResponse> find(PostQuery postQuery) {
        return postWebService.find(postQuery);
    }

    public List<PostResponse> findRelated(PostRelatedQuery postQuery) {
        return postWebService.findRelated(postQuery);
    }

    public void add(String name, ContentEngine engine) {
        engines.put(name, engine);
    }

    public ContentEngine engine(String name) {
        return engines.get(name);
    }
}
