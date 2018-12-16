package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.post.RankType;
import app.jweb.post.api.post.TrendingPostQuery;
import app.jweb.template.Attributes;
import app.jweb.template.BooleanAttribute;
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
public class TrendingPostListComponent extends AbstractPostComponent {
    @Inject
    PostWebService postWebService;

    public TrendingPostListComponent() {
        super("trending-post-list", "component/trending-post-list/trending-post-list.html", ImmutableList.of(
            new StringAttribute("categoryId", null),
            new BooleanAttribute("paginationEnabled", false),
            new StringAttribute("type", null),
            new IntegerAttribute("page", 1),
            new IntegerAttribute("limit", 20)
        ));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        TrendingPostQuery query = new TrendingPostQuery();
        query.page = attributes.get("page");
        query.limit = attributes.get("limit");
        String type = attributes.get("type");
        query.type = type == null ? null : RankType.valueOf(type);
        bindings.put("posts", postWebService.trending(query));
        bindings.put("paginationEnabled", attributes.get("paginationEnabled"));
        bindings.put("path", bindings.request().path());
        template().output(bindings, out);
    }
}
