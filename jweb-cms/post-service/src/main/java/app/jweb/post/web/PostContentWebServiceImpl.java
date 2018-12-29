package app.jweb.post.web;

import app.jweb.post.api.PostContentWebService;
import app.jweb.post.api.content.PostContentResponse;
import app.jweb.post.domain.PostContent;
import app.jweb.post.service.PostContentService;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PostContentWebServiceImpl implements PostContentWebService {
    @Inject
    PostContentService postContentService;

    @Override
    public PostContentResponse getByPostId(String postId) {
        PostContent postContent = postContentService.getByPostId(postId);
        return response(postContent);
    }

    @Override
    public List<PostContentResponse> batchGetByPostIds(List<String> postIds) {
        List<PostContent> postContents = postContentService.batchGetByPostIds(postIds);
        return postContents.stream().map(this::response).collect(Collectors.toList());
    }

    private PostContentResponse response(PostContent postContent) {
        PostContentResponse response = new PostContentResponse();
        response.content = postContent.content;
        response.id = postContent.id;
        response.postId = postContent.postId;
        return response;
    }
}
