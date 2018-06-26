package io.sited.page.web.service;

import io.sited.cache.Cache;
import io.sited.page.api.PageContentWebService;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.content.PageContentResponse;
import io.sited.page.api.draft.DraftResponse;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class CachedPageContentService {
    @Inject
    Cache<PageContentResponse> cache;
    @Inject
    PageContentWebService pageContentWebService;

    @Inject
    PageDraftWebService pageDraftWebService;

    public PageContentResponse content(String pageId, boolean draft) {
        if (draft) {
            DraftResponse response = pageDraftWebService.get(pageId);
            PageContentResponse pageContentResponse = new PageContentResponse();
            pageContentResponse.content = response.content;
            return pageContentResponse;
        }
        Optional<PageContentResponse> cached = cache.get(pageId);
        if (cached.isPresent()) {
            return cached.get();
        }
        PageContentResponse response = pageContentWebService.getByPageId(pageId);
        cache.put(pageId, response);
        return response;
    }

    public void reload(String pageId) {
        cache.delete(pageId);
    }
}
