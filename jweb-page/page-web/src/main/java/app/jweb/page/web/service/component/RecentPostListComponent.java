package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.post.api.PostCategoryWebService;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.post.api.post.PostQuery;
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
public class RecentPostListComponent extends AbstractPostComponent {
    @Inject
    PostWebService postWebService;
    @Inject
    PostCategoryWebService postCategoryWebService;

    public RecentPostListComponent() {
        super("recent-post-list", "component/recent-post-list/recent-post-list.html", ImmutableList.of(
            new StringAttribute("categoryId", null),
            new BooleanAttribute("paginationEnabled", false),
            new IntegerAttribute("page", 1),
            new IntegerAttribute("limit", 20)
        ));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        PostQuery query = new PostQuery();
        query.page = attributes.get("page");
        query.limit = attributes.get("limit");
        query.sortingField = "updatedTime";
        query.desc = true;
        String categoryId = attributes.get("categoryId");
        if (categoryId == null) {
            CategoryResponse category = (CategoryResponse) bindings.get("category");
            if (category != null) {
                query.categoryId = category.id;
                bindings.put("category", category);
            }
        } else {
            CategoryResponse category = postCategoryWebService.get(categoryId);
            query.categoryId = category.id;
            bindings.put("category", category);
        }
        bindings.put("posts", postWebService.find(query));
        bindings.put("paginationEnabled", attributes.get("paginationEnabled"));
        bindings.put("path", bindings.request().path());
        template().output(bindings, out);
    }
}
