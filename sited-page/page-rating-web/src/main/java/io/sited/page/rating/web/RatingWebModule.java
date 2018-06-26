package io.sited.page.rating.web;

import io.sited.page.rating.web.component.PageRatingComponent;
import io.sited.page.rating.web.web.ajax.RatingAJAXController;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class RatingWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().controller(RatingAJAXController.class);
        web().addComponent(requestInjection(new PageRatingComponent()));
    }
}
