package io.sited.page.web.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.PageCommentWebService;
import io.sited.page.api.comment.CommentNodeResponse;
import io.sited.page.api.comment.CommentStatus;
import io.sited.page.api.comment.CommentTreeQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.page.web.PageWebOptions;
import io.sited.page.web.service.UserCommentNodeService;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.TemplateComponent;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class CommentListComponent extends TemplateComponent {
    @Inject
    PageWebOptions options;

    @Inject
    UserCommentNodeService userCommentNodeService;

    @Inject
    PageCommentWebService pageCommentWebService;

    public CommentListComponent() {
        super("comment-list", "component/page-comment-list/page-comment-list.html", Lists.newArrayList(
            new ObjectAttribute<>("page", PageResponse.class, null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        PageResponse page = (PageResponse) attribute("page").value(attributes);
        if (page == null) {
            page = (PageResponse) bindings.get("page");
        }
        if (page == null) {
            return;
        }

        CommentTreeQuery query = new CommentTreeQuery();
        query.pageId = page.id;
        query.limit = 20;
        query.status = CommentStatus.ACTIVE;
        QueryResponse<CommentNodeResponse> comments = pageCommentWebService.tree(query);

        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.putAll(bindings);
        scopedBindings.put("comments", userCommentNodeService.map(comments));
        scopedBindings.put("visitorCommentEnabled", options.visitorCommentEnabled);
        template().output(scopedBindings, out);
    }
}
