package io.sited.comment;

import io.sited.comment.api.CommentModule;
import io.sited.comment.api.PageCommentWebService;
import io.sited.comment.domain.PageComment;
import io.sited.comment.domain.PageCommentVoteTracking;
import io.sited.comment.service.PageCommentService;
import io.sited.comment.service.PageCommentVoteTrackingService;
import io.sited.comment.web.PageCommentWebServiceImpl;
import io.sited.database.DatabaseModule;

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
