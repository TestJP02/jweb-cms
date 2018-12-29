package app.jweb.post.api;


import app.jweb.service.AbstractServiceModule;
import app.jweb.service.ServiceOptions;

/**
 * @author chi
 */
public class PostModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        ServiceOptions options = options("post", ServiceOptions.class);

        api().service(PostCategoryWebService.class, options.url);
        api().service(PostWebService.class, options.url);
        api().service(PostContentWebService.class, options.url);
        api().service(PostStatisticsWebService.class, options.url);
        api().service(PostKeywordWebService.class, options.url);
        api().service(PostTagWebService.class, options.url);
    }
}
