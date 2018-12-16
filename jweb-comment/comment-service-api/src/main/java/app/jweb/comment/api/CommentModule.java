package app.jweb.comment.api;

import app.jweb.service.AbstractServiceModule;
import app.jweb.service.ServiceOptions;

/**
 * @author chi
 */
public class CommentModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        api().service(PageCommentWebService.class, options("comment", ServiceOptions.class).url);
    }
}
