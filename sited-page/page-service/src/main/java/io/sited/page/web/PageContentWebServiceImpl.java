package io.sited.page.web;

import io.sited.page.api.PageContentWebService;
import io.sited.page.api.content.PageContentResponse;
import io.sited.page.domain.PageContent;
import io.sited.page.service.PageContentService;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageContentWebServiceImpl implements PageContentWebService {
    @Inject
    PageContentService pageContentService;

    @Override
    public PageContentResponse getByPageId(String pageId) {
        PageContent pageContent = pageContentService.findByPageId(pageId).orElseThrow(() -> new NotFoundException("missing content, id=" + pageId));
        return response(pageContent);
    }

    @Override
    public List<PageContentResponse> batchGetByPageIds(List<String> pageIds) {
        List<PageContent> pageContents = pageContentService.batchGetByPageIds(pageIds);
        return pageContents.stream().map(this::response).collect(Collectors.toList());
    }

    private PageContentResponse response(PageContent pageContent) {
        PageContentResponse response = new PageContentResponse();
        response.content = pageContent.content;
        response.id = pageContent.id;
        response.pageId = pageContent.pageId;
        return response;
    }
}
