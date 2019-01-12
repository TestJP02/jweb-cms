package app.jweb.page.api;


import app.jweb.service.AbstractServiceModule;
import app.jweb.service.ServiceOptions;

/**
 * @author chi
 */
public class PageModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        ServiceOptions options = options("page", ServiceOptions.class);

        api().service(PageVariableWebService.class, options.url);
        api().service(PageSavedComponentWebService.class, options.url);
        api().service(PageWebService.class, options.url);
        api().service(PageComponentWebService.class, options.url);
        api().service(PageTemplateWebService.class, options.url);
        api().service(PageStatisticsWebService.class, options.url);
        api().service(PageCategoryWebService.class, options.url);
        api().service(PageKeywordWebService.class, options.url);
        api().service(PageDraftWebService.class, options.url);
    }
}
