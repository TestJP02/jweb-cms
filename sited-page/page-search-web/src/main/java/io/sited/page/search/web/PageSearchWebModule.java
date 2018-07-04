package io.sited.page.search.web;

import io.sited.page.search.web.component.SearchPageResultComponent;
import io.sited.page.search.web.web.PageSearchController;
import io.sited.web.AbstractWebModule;

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
