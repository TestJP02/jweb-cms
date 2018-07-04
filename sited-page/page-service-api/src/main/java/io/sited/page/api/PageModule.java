package io.sited.page.api;


import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceOptions;

/**
 * @author chi
 */
public class PageModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        ServiceOptions options = options("page", ServiceOptions.class);

        api().service(PageCategoryWebService.class, options.url);
        api().service(PageWebService.class, options.url);
        api().service(PageVariableWebService.class, options.url);
        api().service(PageTemplateWebService.class, options.url);
        api().service(PageComponentWebService.class, options.url);
        api().service(PageContentWebService.class, options.url);
        api().service(PageStatisticsWebService.class, options.url);

        api().service(PageKeywordWebService.class, options.url);
        api().service(PageTagWebService.class, options.url);
    }
}
