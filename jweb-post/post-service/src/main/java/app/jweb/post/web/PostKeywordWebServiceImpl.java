package app.jweb.post.web;

import app.jweb.post.api.PostKeywordWebService;
import app.jweb.post.api.keyword.KeywordResponse;
import app.jweb.post.domain.PostKeyword;
import app.jweb.post.service.PostKeywordService;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PostKeywordWebServiceImpl implements PostKeywordWebService {
    @Inject
    PostKeywordService keywordService;

    @Override
    public List<KeywordResponse> find() {
        return keywordService.find().stream().map(this::response).collect(Collectors.toList());
    }

    private KeywordResponse response(PostKeyword keyword) {
        KeywordResponse response = new KeywordResponse();
        response.value = keyword.keyword;
        response.path = keyword.path;
        return response;
    }
}
