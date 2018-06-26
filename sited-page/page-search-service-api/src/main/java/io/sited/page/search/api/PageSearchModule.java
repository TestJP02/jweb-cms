package io.sited.page.search.api;

import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceOptions;

import java.util.List;

/**
 * @author chi
 */
public class PageSearchModule extends AbstractServiceModule {
    public PageSearchModule() {
        super();
    }

    protected PageSearchModule(String name, List<String> dependencies) {
        super(name, dependencies);
    }

    @Override
    protected void configure() {
        ServiceOptions options = options("page-search", ServiceOptions.class);
        api().service(PageSearchWebService.class, options.url);
    }
}
