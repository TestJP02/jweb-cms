package io.sited.comment.web;

import io.sited.comment.web.service.UserCommentNodeService;
import io.sited.comment.web.service.component.CommentListComponent;
import io.sited.comment.web.web.api.CommentWebController;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class CommentWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        bind(CommentWebOptions.class).toInstance(options("comment-web", CommentWebOptions.class));
        bind(UserCommentNodeService.class);

        message("conf/messages/comment-web");

        web().addComponent(requestInjection(new CommentListComponent()));
        web().controller(CommentWebController.class);
    }
}
