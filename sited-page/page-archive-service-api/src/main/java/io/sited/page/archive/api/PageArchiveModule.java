package io.sited.page.archive.api;

import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceOptions;

/**
 * @author chi
 */
public class PageArchiveModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        api().service(PageArchiveWebService.class, options("page-archive", ServiceOptions.class).url);
    }
}
