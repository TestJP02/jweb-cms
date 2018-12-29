package app.jweb.post.web;

import app.jweb.post.api.PostDraftWebService;
import app.jweb.post.api.draft.BatchDeleteDraftRequest;
import app.jweb.post.api.draft.CreateDraftRequest;
import app.jweb.post.api.draft.DraftQuery;
import app.jweb.post.api.draft.DraftResponse;
import app.jweb.post.api.draft.UpdateDraftRequest;
import app.jweb.post.api.post.PostResponse;
import app.jweb.post.domain.Post;
import app.jweb.post.domain.PostDraft;
import app.jweb.post.service.PostDraftService;
import app.jweb.post.service.PostService;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class PostDraftWebServiceImpl implements PostDraftWebService {
    @Inject
    PostDraftService postDraftService;
    @Inject
    PostService postService;


    @Override
    public DraftResponse get(String id) {
        return response(postDraftService.get(id));
    }

    @Override
    public QueryResponse<DraftResponse> find(DraftQuery query) {
        return postDraftService.find(query).map(this::response);
    }

    @Override
    public Optional<DraftResponse> findByPostId(String postId) {
        Optional<PostDraft> draft = postDraftService.findByPostId(postId);
        return draft.map(this::response);
    }

    @Override
    public Optional<DraftResponse> findByPath(String path) {
        return postDraftService.findByPath(path).map(this::response);
    }

    @Override
    public DraftResponse create(CreateDraftRequest request) {
        PostDraft pageDraft = postDraftService.create(request);
        return response(pageDraft);
    }

    @Override
    public DraftResponse update(String id, UpdateDraftRequest request) {
        PostDraft draft = postDraftService.update(id, request);
        return response(draft);
    }

    @Override
    public PostResponse publish(String id, String requestBy) {
        return response(postService.publish(id, requestBy));
    }

    @Override
    public void batchDelete(BatchDeleteDraftRequest request) {
        for (String id : request.ids) {
            postDraftService.delete(id);
        }
    }

    private DraftResponse response(PostDraft draft) {
        DraftResponse response = new DraftResponse();
        response.id = draft.id;
        response.postId = draft.postId;
        response.userId = draft.userId;
        response.categoryId = draft.categoryId;
        response.path = draft.path;
        response.templatePath = draft.templatePath;
        response.tags = draft.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(draft.tags);
        response.keywords = draft.keywords == null ? ImmutableList.of() : Splitter.on(';').splitToList(draft.keywords);
        response.fields = draft.fields == null ? ImmutableMap.of() : JSON.fromJSON(draft.fields, Map.class);
        response.content = draft.content;
        response.title = draft.title;
        response.description = draft.description;
        response.imageURL = draft.imageURL;
        response.imageURLs = draft.imageURLs == null ? ImmutableList.of() : Splitter.on(";").splitToList(draft.imageURLs);
        response.userId = draft.userId;
        response.version = null;
        response.status = draft.status;
        response.topFixed = draft.topFixed;
        response.createdTime = draft.createdTime;
        response.createdBy = draft.createdBy;
        response.updatedTime = draft.updatedTime;
        response.updatedBy = draft.updatedBy;
        return response;
    }


    private PostResponse response(Post post) {
        PostResponse response = new PostResponse();
        response.id = post.id;
        response.userId = post.userId;
        response.categoryId = post.categoryId;
        response.path = post.path;
        response.templatePath = post.templatePath;
        response.tags = post.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(post.tags);
        response.keywords = post.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(post.keywords);
        response.fields = post.fields == null ? ImmutableMap.of() : JSON.fromJSON(post.fields, Map.class);
        response.title = post.title;
        response.description = post.description;
        response.imageURL = post.imageURL;
        response.imageURLs = post.imageURLs == null ? ImmutableList.of() : Splitter.on(";").splitToList(post.imageURLs);
        response.version = post.version;
        response.status = post.status;
        response.topFixed = post.topFixed;
        response.createdTime = post.createdTime;
        response.createdBy = post.createdBy;
        response.updatedTime = post.updatedTime;
        response.updatedBy = post.updatedBy;
        return response;
    }
}
