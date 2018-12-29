package app.jweb.page.rating;


import app.jweb.page.rating.api.PageRatingModule;
import app.jweb.page.rating.api.RatingTrackingWebService;
import app.jweb.page.rating.api.RatingWebService;
import app.jweb.database.DatabaseConfig;
import app.jweb.database.DatabaseModule;
import app.jweb.page.rating.domain.PageRating;
import app.jweb.page.rating.domain.PageRatingTracking;
import app.jweb.page.rating.service.PageRatingService;
import app.jweb.page.rating.service.PageRatingTrackingService;
import app.jweb.page.rating.web.RatingTrackingWebServiceImpl;
import app.jweb.page.rating.web.RatingWebServiceImpl;

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
