package app.jweb.page.rating.api;


import app.jweb.service.AbstractServiceModule;
import app.jweb.service.ServiceOptions;

public class PageRatingModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        ServiceOptions options = options("page-rating", ServiceOptions.class);
        api().service(RatingWebService.class, options.url);
        api().service(RatingTrackingWebService.class, options.url);
    }
}
