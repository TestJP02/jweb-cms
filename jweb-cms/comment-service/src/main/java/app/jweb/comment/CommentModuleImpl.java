package app.jweb.comment;

import app.jweb.comment.api.CommentModule;
import app.jweb.comment.api.PageCommentWebService;
import app.jweb.comment.domain.PageComment;
import app.jweb.comment.domain.PageCommentVoteTracking;
import app.jweb.comment.service.PageCommentService;
import app.jweb.comment.service.PageCommentVoteTrackingService;
import app.jweb.comment.web.PageCommentWebServiceImpl;
import app.jweb.database.DatabaseModule;

/**
 * @author chi
 */
public class CommentModuleImpl extends CommentModule {
    @Override
    protected void configure() {
        module(DatabaseModule.class)
            .entity(PageComment.class)
            .entity(PageCommentVoteTracking.class);

        bind(PageCommentService.class);
        bind(PageCommentVoteTrackingService.class);

        api().service(PageCommentWebService.class, PageCommentWebServiceImpl.class);
    }
}
