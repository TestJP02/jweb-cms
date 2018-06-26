package io.sited.page.tracking.web;

import io.sited.page.tracking.web.component.PopularPageListComponent;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class PageTrackingWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        message("conf/messages/page-tracking-web");
        web().addComponent(requestInjection(new PopularPageListComponent()));
    }
}
