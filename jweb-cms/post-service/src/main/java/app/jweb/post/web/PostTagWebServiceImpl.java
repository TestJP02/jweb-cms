package app.jweb.post.web;

import app.jweb.post.api.PostTagWebService;
import app.jweb.post.api.tag.BatchCreatePostTagRequest;
import app.jweb.post.api.tag.BatchDeletePostTagRequest;
import app.jweb.post.api.tag.BatchGetTagRequest;
import app.jweb.post.api.tag.CreatePostTagRequest;
import app.jweb.post.api.tag.PostTagNodeResponse;
import app.jweb.post.api.tag.PostTagQuery;
import app.jweb.post.api.tag.PostTagResponse;
import app.jweb.post.api.tag.PostTagTreeQuery;
import app.jweb.post.api.tag.UpdatePostTagRequest;
import app.jweb.post.domain.PostTag;
import app.jweb.post.service.PostTagService;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PostTagWebServiceImpl implements PostTagWebService {
    @Inject
    PostTagService postTagService;

    @Override
    public PostTagResponse get(String id) {
        return response(postTagService.get(id));
    }

    @Override
    public List<PostTagResponse> batchGet(BatchGetTagRequest request) {
        List<PostTag> tags = postTagService.batchGet(request);
        return tags.stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public Optional<PostTagResponse> findByName(String name) {
        return postTagService.findByDisplayName(name).map(this::response);
    }

    @Override
    public QueryResponse<PostTagResponse> find(PostTagQuery query) {
        QueryResponse<PostTag> response = postTagService.find(query);
        return response.map(this::response);
    }

    @Override
    public List<PostTagNodeResponse> tree(PostTagTreeQuery query) {
        List<PostTag> list = postTagService.find(query);
        Map<String, PostTagNodeResponse> index = Maps.newHashMap();
        List<PostTagNodeResponse> firstLevels = Lists.newArrayList();
        list.forEach(tag -> {
            PostTagNodeResponse node = new PostTagNodeResponse();
            node.tag = response(tag);
            node.children = Lists.newArrayList();
            if (Objects.equals(tag.parentId, query.parentId)) {
                firstLevels.add(node);
            }
            index.put(node.tag.id, node);
        });
        buildTree(index);
        return firstLevels;
    }

    @Override
    public List<PostTagResponse> children(String id) {
        return postTagService.children(id).stream().map(this::response).collect(Collectors.toList());
    }

    private void buildTree(Map<String, PostTagNodeResponse> index) {
        index.values().forEach(tag -> {
            if (tag.tag.parentId != null) {
                PostTagNodeResponse parent = index.get(tag.tag.parentId);
                if (parent != null) {
                    parent.children.add(index.get(tag.tag.id));
                }
            }
        });
    }

    @Override
    public PostTagResponse create(CreatePostTagRequest request) {
        return response(postTagService.create(request));
    }

    @Override
    public List<PostTagResponse> batchCreate(BatchCreatePostTagRequest request) {
        List<PostTag> postTags = postTagService.batchCreate(request);
        return postTags.stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public PostTagResponse update(String id, UpdatePostTagRequest request) {
        return response(postTagService.update(id, request));
    }

    @Override
    public void batchDelete(BatchDeletePostTagRequest request) {
        postTagService.batchDelete(request);
    }

    private PostTagResponse response(PostTag postTag) {
        PostTagResponse response = new PostTagResponse();
        response.id = postTag.id;
        response.name = postTag.name;
        response.parentId = postTag.parentId;
        response.parentIds = postTag.parentIds == null ? ImmutableList.of() : Splitter.on(";").splitToList(postTag.parentIds);
        response.displayName = postTag.displayName;
        response.displayOrder = postTag.displayOrder;
        response.imageURL = postTag.imageURL;
        response.type = postTag.type;
        response.level = postTag.level;
        response.tags = postTag.tags == null ? ImmutableList.of() : Splitter.on(";").splitToList(postTag.tags);
        response.fields = postTag.fields == null ? ImmutableMap.of() : JSON.fromJSON(postTag.fields, Map.class);
        response.alias = postTag.alias;
        response.description = postTag.description;
        response.totalTagged = postTag.totalTagged;
        response.status = postTag.status;
        response.createdTime = postTag.createdTime;
        response.updatedTime = postTag.updatedTime;
        response.createdBy = postTag.createdBy;
        response.updatedBy = postTag.updatedBy;
        return response;
    }
}
