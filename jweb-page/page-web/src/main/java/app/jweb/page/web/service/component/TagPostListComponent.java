package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.page.web.service.PostService;
import app.jweb.post.api.PostTagWebService;
import app.jweb.post.api.post.PostQuery;
import app.jweb.post.api.post.PostStatus;
import app.jweb.post.api.tag.PostTagResponse;
import app.jweb.template.Attributes;
import app.jweb.template.BooleanAttribute;
import app.jweb.template.Children;
import app.jweb.template.IntegerAttribute;
import app.jweb.template.StringAttribute;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class TagPostListComponent extends AbstractPostComponent {
    @Inject
    PostService postService;
    @Inject
    PostTagWebService tagWebService;

    public TagPostListComponent() {
        super("tag-post-list", "component/tag-post-list/tag-post-list.html", ImmutableList.of(
            new StringAttribute("tag", null),
            new BooleanAttribute("paginationEnabled", false),
            new IntegerAttribute("page", 1),
            new IntegerAttribute("limit", 20)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);

        String tag = attributes.get("tag");
        if (tag == null) {
            tag = bindings.request().pathParam("tag");
        }
        PostTagResponse response = tagWebService.findByName(tag).orElse(null);
        if (response == null) {
            return;
        }

        PostQuery postQuery = new PostQuery();
        postQuery.tags = Lists.newArrayList(tag);
        postQuery.page = attributes.get("page");
        postQuery.limit = attributes.get("limit");
        postQuery.status = PostStatus.ACTIVE;

        bindings.put("posts", postService.find(postQuery));
        bindings.put("path", "/tag/" + response.name);
        bindings.put("paginationEnabled", attributes.get("paginationEnabled"));
        template().output(bindings, out);
    }
}
