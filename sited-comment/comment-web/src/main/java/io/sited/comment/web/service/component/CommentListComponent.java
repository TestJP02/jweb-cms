package io.sited.comment.web.service.component;

import io.sited.comment.api.PageCommentWebService;
import io.sited.comment.api.comment.CommentNodeResponse;
import io.sited.comment.api.comment.CommentStatus;
import io.sited.comment.api.comment.CommentTreeQuery;
import io.sited.comment.web.CommentWebOptions;
import io.sited.comment.web.service.UserCommentNodeService;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.Bindings;
import io.sited.page.web.PageInfo;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class CommentListComponent extends AbstractPageComponent {
    @Inject
    UserCommentNodeService userCommentNodeService;

    @Inject
    PageCommentWebService pageCommentWebService;

    @Inject
    CommentWebOptions options;

    public CommentListComponent() {
        super("comment-list", "component/page-comment-list/page-comment-list.html");
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        PageInfo page = bindings.page();

        CommentTreeQuery query = new CommentTreeQuery();
        query.pageId = page.id();
        query.limit = 20;
        query.status = CommentStatus.ACTIVE;
        QueryResponse<CommentNodeResponse> comments = pageCommentWebService.tree(query);

        bindings.putAll(attributes);
        bindings.put("comments", userCommentNodeService.map(comments));
        bindings.put("visitorCommentEnabled", options.visitorCommentEnabled);
        template().output(bindings, out);
    }
}
