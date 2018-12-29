package app.jweb.page.search.web;

import app.jweb.page.search.web.component.SearchPageResultComponent;
import app.jweb.page.search.web.web.PageSearchController;
import app.jweb.web.AbstractWebModule;

/**
 * @author chi
 */
public class PageSearchWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().controller(PageSearchController.class);
        web().addComponent(requestInjection(new SearchPageResultComponent()));
        message("conf/messages/page-search-web");
    }
}
