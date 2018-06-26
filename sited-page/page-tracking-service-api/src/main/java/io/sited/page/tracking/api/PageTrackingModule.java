package io.sited.page.tracking.api;

import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceOptions;

/**
 * @author chi
 */
public class PageTrackingModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        ServiceOptions options = options("page-tracking", ServiceOptions.class);
        api().service(PageTrackingWebService.class, options.url);
    }
}
