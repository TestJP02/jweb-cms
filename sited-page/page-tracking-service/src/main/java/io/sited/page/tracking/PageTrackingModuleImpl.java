package io.sited.page.tracking;

import io.sited.database.DatabaseConfig;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageConfig;
import io.sited.message.MessageModule;
import io.sited.message.TopicOptions;
import io.sited.page.api.page.PageVisitedMessage;
import io.sited.page.tracking.api.PageTrackingModule;
import io.sited.page.tracking.api.PageTrackingStatisticsWebService;
import io.sited.page.tracking.api.PageTrackingWebService;
import io.sited.page.tracking.domain.PageDailyTracking;
import io.sited.page.tracking.domain.PageMonthlyTracking;
import io.sited.page.tracking.domain.PageTracking;
import io.sited.page.tracking.domain.PageWeeklyTracking;
import io.sited.page.tracking.message.PageVisitedMessageHandler;
import io.sited.page.tracking.service.PageTrackingService;
import io.sited.page.tracking.web.PageTrackingStatisticsWebServiceImpl;
import io.sited.page.tracking.web.PageTrackingWebServiceImpl;

/**
 * @author chi
 */
public class PageTrackingModuleImpl extends PageTrackingModule {
    @Override
    protected void configure() {
        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig
            .entity(PageTracking.class)
            .entity(PageDailyTracking.class)
            .entity(PageWeeklyTracking.class)
            .entity(PageMonthlyTracking.class);

        bind(PageTrackingService.class);

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.createTopic(PageVisitedMessage.class, new TopicOptions());
        messageConfig.listen(PageVisitedMessage.class, PageVisitedMessageHandler.class);

        api().service(PageTrackingWebService.class, PageTrackingWebServiceImpl.class);
        api().service(PageTrackingStatisticsWebService.class, PageTrackingStatisticsWebServiceImpl.class);
    }
}
