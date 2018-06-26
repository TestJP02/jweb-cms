package io.sited.page.rating.api;


import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceOptions;

public class PageRatingModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        ServiceOptions options = options("page-rating", ServiceOptions.class);
        api().service(RatingWebService.class, options.url);
        api().service(RatingTrackingWebService.class, options.url);
    }
}
