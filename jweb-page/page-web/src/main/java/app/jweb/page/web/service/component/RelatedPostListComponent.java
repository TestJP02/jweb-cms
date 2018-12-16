package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.page.web.PostInfo;
import app.jweb.page.web.service.PostService;
import app.jweb.post.api.post.PostRelatedQuery;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.IntegerAttribute;
import app.jweb.template.StringAttribute;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class RelatedPostListComponent extends AbstractPostComponent {
    @Inject
    PostService postService;

    public RelatedPostListComponent() {
        super("related-post-list", "component/related-post-list/related-post-list.html", ImmutableList.of(
            new StringAttribute("title", null),
            new IntegerAttribute("limit", 5)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        PostInfo post = bindings.post();

        PostRelatedQuery postRelatedQuery = new PostRelatedQuery();
        postRelatedQuery.limit = attributes.get("limit");
        postRelatedQuery.id = post.id();
        bindings.put("posts", postService.findRelated(postRelatedQuery));
        template().output(bindings, out);
    }
}
