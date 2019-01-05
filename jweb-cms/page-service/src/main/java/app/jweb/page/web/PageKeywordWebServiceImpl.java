package app.jweb.page.web;

import app.jweb.page.api.PageKeywordWebService;
import app.jweb.page.api.keyword.KeywordResponse;
import app.jweb.page.domain.PageKeyword;
import app.jweb.page.service.PageKeywordService;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageKeywordWebServiceImpl implements PageKeywordWebService {
    @Inject
    PageKeywordService keywordService;

    @Override
    public List<KeywordResponse> find() {
        return keywordService.find().stream().map(this::response).collect(Collectors.toList());
    }

    private KeywordResponse response(PageKeyword keyword) {
        KeywordResponse response = new KeywordResponse();
        response.value = keyword.keyword;
        response.path = keyword.path;
        return response;
    }
}
