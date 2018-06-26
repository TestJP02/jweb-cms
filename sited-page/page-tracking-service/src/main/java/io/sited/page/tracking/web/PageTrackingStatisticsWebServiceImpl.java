package io.sited.page.tracking.web;

import io.sited.page.tracking.api.PageTrackingStatisticsWebService;
import io.sited.page.tracking.api.tracking.PageStatisticsQuery;
import io.sited.page.tracking.api.tracking.PageStatisticsResponse;
import io.sited.page.tracking.service.PageTrackingService;

import javax.inject.Inject;
import java.util.List;

/**
 * @author chi
 */
public class PageTrackingStatisticsWebServiceImpl implements PageTrackingStatisticsWebService {
    @Inject
    PageTrackingService pageTrackingService;

    @Override
    public List<PageStatisticsResponse> find(PageStatisticsQuery query) {
        return pageTrackingService.find(query);
    }
}
