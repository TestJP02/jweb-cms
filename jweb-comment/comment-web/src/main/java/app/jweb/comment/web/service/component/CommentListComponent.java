package app.jweb.comment.web.service.component;

import app.jweb.comment.api.PageCommentWebService;
import app.jweb.comment.api.comment.CommentNodeResponse;
import app.jweb.comment.api.comment.CommentStatus;
import app.jweb.comment.api.comment.CommentTreeQuery;
import app.jweb.comment.web.CommentWebOptions;
import app.jweb.comment.web.service.UserCommentNodeService;
import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.page.web.PostInfo;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class CommentListComponent extends AbstractPostComponent {
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
        PostInfo page = bindings.post();

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
