package app.jweb.comment.web;

import app.jweb.comment.web.service.UserCommentNodeService;
import app.jweb.comment.web.service.component.CommentListComponent;
import app.jweb.comment.web.web.api.CommentWebController;
import app.jweb.web.AbstractWebModule;

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
