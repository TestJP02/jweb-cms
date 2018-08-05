package io.sited.comment.api;

import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceOptions;

/**
 * @author chi
 */
public class CommentModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        api().service(PageCommentWebService.class, options("comment", ServiceOptions.class).url);
    }
}
