package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.post.PostQuery;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.StringAttribute;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class AuthorComponent extends AbstractPostComponent {
    @Inject
    PostWebService postWebService;

    public AuthorComponent() {
        super("author", "component/author/author.html", Lists.newArrayList(
            new StringAttribute("title", null),
            new StringAttribute("userId", null)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);

        String userId = attributes.get("userId");
        if (userId == null) {
            userId = bindings.post().userId();
        }

        bindings.put("userId", userId);
        PostQuery query = new PostQuery();
        query.userId = userId;
        query.limit = 5;
        query.sortingField = "updatedTime";
        query.desc = true;
        bindings.put("posts", postWebService.find(query));

        template().output(bindings, out);
    }
}
