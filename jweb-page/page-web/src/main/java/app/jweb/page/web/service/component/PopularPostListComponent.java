package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.post.PopularPostQuery;
import app.jweb.post.api.post.PopularPostResponse;
import app.jweb.post.api.post.RankType;
import app.jweb.template.Attributes;
import app.jweb.template.BooleanAttribute;
import app.jweb.template.Children;
import app.jweb.template.IntegerAttribute;
import app.jweb.template.StringAttribute;
import app.jweb.util.collection.QueryResponse;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class PopularPostListComponent extends AbstractPostComponent {
    @Inject
    PostWebService postWebService;

    public PopularPostListComponent() {
        super("popular-post-list", "component/popular-post-list/popular-post-list.html", Lists.newArrayList(
            new IntegerAttribute("limit", 10),
            new BooleanAttribute("paginationEnabled", false),
            new StringAttribute("type", "DAILY")
        ));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        String page = bindings.request().queryParam("page").orElse(null);
        String type = attributes.get("type");

        PopularPostQuery query = new PopularPostQuery();
        query.type = type == null ? null : RankType.valueOf(type);
        query.page = page == null ? 1 : Integer.parseInt(page);
        query.limit = attributes.get("limit");
        QueryResponse<PopularPostResponse> posts = postWebService.popular(query);
        bindings.put("posts", posts);

        bindings.put("path", bindings.request().path());
        bindings.put("paginationEnabled", attributes.get("paginationEnabled"));
        template().output(bindings, out);
    }
}
