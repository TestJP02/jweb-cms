package io.sited.page.tracking.web;

import io.sited.page.tracking.api.PageTrackingWebService;
import io.sited.page.tracking.api.tracking.PageTrackingQuery;
import io.sited.page.tracking.api.tracking.PageTrackingResponse;
import io.sited.page.tracking.service.PageTrackingService;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageTrackingWebServiceImpl implements PageTrackingWebService {
    @Inject
    PageTrackingService pageTrackingService;

    @Override
    public QueryResponse<PageTrackingResponse> find(PageTrackingQuery query) {
        return pageTrackingService.find(query);
    }
}
