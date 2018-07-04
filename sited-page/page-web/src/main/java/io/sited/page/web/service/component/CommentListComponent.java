package io.sited.page.web.service.component;

import io.sited.page.api.PageCommentWebService;
import io.sited.page.api.comment.CommentNodeResponse;
import io.sited.page.api.comment.CommentStatus;
import io.sited.page.api.comment.CommentTreeQuery;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.ComponentBindings;
import io.sited.page.web.PageInfo;
import io.sited.page.web.PageWebOptions;
import io.sited.page.web.service.UserCommentNodeService;
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
    PageWebOptions options;

    @Inject
    UserCommentNodeService userCommentNodeService;

    @Inject
    PageCommentWebService pageCommentWebService;

    public CommentListComponent() {
        super("comment-list", "component/page-comment-list/page-comment-list.html");
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
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
