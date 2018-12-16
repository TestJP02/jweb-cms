package app.jweb.post.web;


import app.jweb.post.api.PostWebService;
import app.jweb.post.api.post.BatchGetPostRequest;
import app.jweb.post.api.post.CreatePostRequest;
import app.jweb.post.api.post.DeletePostRequest;
import app.jweb.post.api.post.PopularPostQuery;
import app.jweb.post.api.post.PopularPostResponse;
import app.jweb.post.api.post.PostNavigationResponse;
import app.jweb.post.api.post.PostQuery;
import app.jweb.post.api.post.PostRelatedQuery;
import app.jweb.post.api.post.PostResponse;
import app.jweb.post.api.post.PostStatus;
import app.jweb.post.api.post.RevertDeletePostRequest;
import app.jweb.post.api.post.TopFixedPostQuery;
import app.jweb.post.api.post.TrendingPostQuery;
import app.jweb.post.api.post.UpdatePostRequest;
import app.jweb.post.domain.Post;
import app.jweb.post.domain.PostContent;
import app.jweb.post.domain.PostStatistics;
import app.jweb.post.service.PostContentService;
import app.jweb.post.service.PostDraftService;
import app.jweb.post.service.PostService;
import app.jweb.post.service.PostStatisticsService;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PostWebServiceImpl implements PostWebService {
    @Inject
    PostService postService;
    @Inject
    PostDraftService postDraftService;
    @Inject
    PostContentService postContentService;
    @Inject
    PostStatisticsService postStatisticsService;

    @Override
    public PostResponse get(String id) {
        return response(postService.get(id), postContentService.getByPostId(id));
    }

    @Override
    public List<PostResponse> batchGet(BatchGetPostRequest request) {
        return postService.batchGet(request.ids).stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public Optional<PostResponse> findByPath(String path) {
        return postService.findByPath(path).map(post -> response(post, postContentService.getByPostId(post.id)));
    }

    @Override
    public QueryResponse<PostResponse> find(PostQuery query) {
        return postService.find(query).map(this::response);
    }

    @Override
    public List<PostResponse> findRelated(PostRelatedQuery query) {
        List<Post> posts = postService.findRelated(query);
        return posts.stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public PostNavigationResponse navigation(String id) {
        PostNavigationResponse postNavigationResponse = new PostNavigationResponse();
        postNavigationResponse.next = postService.findNext(id).map(this::response).orElse(null);
        postNavigationResponse.prev = postService.findPrev(id).map(this::response).orElse(null);
        return postNavigationResponse;
    }

    @Override
    public PostResponse create(CreatePostRequest request) {
        return response(postService.create(request));
    }

    @Override
    public PostResponse update(String id, UpdatePostRequest request) {
        return response(postService.update(id, request));
    }

    @Override
    public void batchDelete(DeletePostRequest request) {
        if (request.posts == null) {
            return;
        }

        for (DeletePostRequest.PostView post : request.posts) {
            if (post.status == PostStatus.DRAFT || post.status == PostStatus.AUDITING) {
                postDraftService.delete(post.id);
            } else {
                postService.delete(post.id, request.requestBy);
            }
        }
    }

    @Override
    public void revertDelete(RevertDeletePostRequest request) {
        request.ids.forEach(id -> postService.revert(id, request.requestBy));
    }

    @Override
    public QueryResponse<PopularPostResponse> popular(PopularPostQuery query) {
        QueryResponse<PostStatistics> statistics = postStatisticsService.find(query);
        Map<String, Post> posts = postService
            .batchGet(statistics.items.stream().map(statistic -> statistic.id).collect(Collectors.toList()))
            .stream().collect(Collectors.toMap(post -> post.id, post -> post));

        QueryResponse<PopularPostResponse> popularPosts = new QueryResponse<>();
        popularPosts.total = statistics.total;
        popularPosts.limit = statistics.limit;
        popularPosts.page = statistics.page;
        popularPosts.items = Lists.newArrayListWithExpectedSize(statistics.items.size());
        for (PostStatistics statistic : statistics) {
            Post post = posts.get(statistic.id);
            if (post != null) {
                PopularPostResponse popularPostResponse = popularPost(post, statistic);
                popularPosts.items.add(popularPostResponse);
            }
        }
        return popularPosts;
    }

    @Override
    public QueryResponse<PostResponse> topFixed(TopFixedPostQuery query) {
        QueryResponse<Post> posts = postService.topFixed(query);
        return posts.map(this::response);
    }

    @Override
    public QueryResponse<PostResponse> trending(TrendingPostQuery query) {
        QueryResponse<PostStatistics> statistics = postStatisticsService.find(query);
        QueryResponse<PostResponse> posts = new QueryResponse<>();
        posts.total = statistics.total;
        posts.limit = statistics.limit;
        posts.page = statistics.page;
        posts.items = postService.batchGet(statistics.items.stream().map(statistic -> statistic.id).collect(Collectors.toList()))
            .stream().map(this::response).collect(Collectors.toList());
        return posts;
    }

    private PostResponse response(Post post) {
        PostResponse response = new PostResponse();
        response.id = post.id;
        response.userId = post.userId;
        response.categoryId = post.categoryId;
        response.tags = post.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(post.tags);
        response.keywords = post.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(post.keywords);
        response.fields = post.fields == null ? ImmutableMap.of() : JSON.fromJSON(post.fields, Map.class);
        response.imageURLs = post.imageURLs == null ? ImmutableList.of() : Splitter.on(";").splitToList(post.imageURLs);
        response.path = post.path;
        response.templatePath = post.templatePath;
        response.title = post.title;
        response.description = post.description;
        response.imageURL = post.imageURL;
        response.version = post.version;
        response.topFixed = post.topFixed;
        response.status = post.status;
        response.createdTime = post.createdTime;
        response.createdBy = post.createdBy;
        response.updatedTime = post.updatedTime;
        response.updatedBy = post.updatedBy;
        return response;
    }

    private PostResponse response(Post post, PostContent content) {
        PostResponse response = response(post);
        response.content = content.content;
        return response;
    }

    private PopularPostResponse popularPost(Post post, PostStatistics postStatistics) {
        PopularPostResponse popularPostResponse = new PopularPostResponse();
        popularPostResponse.id = post.id;
        popularPostResponse.userId = post.userId;
        popularPostResponse.categoryId = post.categoryId;
        popularPostResponse.imageURL = post.imageURL;
        popularPostResponse.path = post.path;
        popularPostResponse.title = post.title;
        popularPostResponse.tags = post.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(post.tags);
        popularPostResponse.fields = post.fields == null ? ImmutableMap.of() : JSON.fromJSON(post.fields, Map.class);
        popularPostResponse.description = post.description;
        popularPostResponse.createdTime = post.createdTime;
        popularPostResponse.updatedTime = post.updatedTime;
        popularPostResponse.totalCommented = postStatistics.totalCommented;
        popularPostResponse.totalLiked = postStatistics.totalLiked;
        popularPostResponse.totalDisliked = postStatistics.totalDisliked;
        return popularPostResponse;
    }
}
