package io.sited.page.web;

import io.sited.page.api.PageKeywordWebService;
import io.sited.page.api.keyword.KeywordResponse;
import io.sited.page.domain.PageKeyword;
import io.sited.page.service.PageKeywordService;

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
