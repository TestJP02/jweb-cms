package io.sited.comment.admin;

import io.sited.admin.AbstractAdminModule;
import io.sited.comment.admin.web.PageCommentAdminController;

/**
 * @author chi
 */
public class CommentAdminModule extends AbstractAdminModule {
    @Override
    protected void configure() {
        admin().controller(PageCommentAdminController.class);

        admin().bundle("pageBundle")
            .addMessages("conf/messages/comment-admin")
            .addRoute("/admin/page/:pageId/comments", "pageCommentBundle")
            .addScriptFiles("admin/static/comment/pageComment.min.js");
    }
}
