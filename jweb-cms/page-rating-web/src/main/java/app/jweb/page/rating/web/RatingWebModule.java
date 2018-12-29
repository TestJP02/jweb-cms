package app.jweb.page.rating.web;

import app.jweb.page.rating.web.component.PageRatingComponent;
import app.jweb.page.rating.web.web.PageRatingWebController;
import app.jweb.web.AbstractWebModule;

/**
 * @author chi
 */
public class RatingWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().controller(PageRatingWebController.class);
        web().addComponent(requestInjection(new PageRatingComponent()));
    }
}
