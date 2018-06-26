package io.sited.page.rating;


import io.sited.database.DatabaseConfig;
import io.sited.database.DatabaseModule;
import io.sited.page.rating.api.PageRatingModule;
import io.sited.page.rating.api.RatingTrackingWebService;
import io.sited.page.rating.api.RatingWebService;
import io.sited.page.rating.domain.PageRating;
import io.sited.page.rating.domain.PageRatingTracking;
import io.sited.page.rating.service.PageRatingService;
import io.sited.page.rating.service.PageRatingTrackingService;
import io.sited.page.rating.web.RatingTrackingWebServiceImpl;
import io.sited.page.rating.web.RatingWebServiceImpl;

/**
 * @author chi
 */
public class PageRatingModuleImpl extends PageRatingModule {
    @Override
    protected void configure() {
        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig
            .entity(PageRating.class)
            .entity(PageRatingTracking.class);

        bind(PageRatingTrackingService.class);
        bind(PageRatingService.class);

        api().service(RatingWebService.class, RatingWebServiceImpl.class);
        api().service(RatingTrackingWebService.class, RatingTrackingWebServiceImpl.class);
    }
}
