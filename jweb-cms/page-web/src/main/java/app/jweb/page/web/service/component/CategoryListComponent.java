package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.post.api.PostCategoryWebService;
import app.jweb.post.api.category.CategoryQuery;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.IntegerAttribute;
import app.jweb.template.StringAttribute;
import app.jweb.util.collection.QueryResponse;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

/**
 * @author chi
 */
public class CategoryListComponent extends AbstractPostComponent {
    @Inject
    PostCategoryWebService postCategoryWebService;

    public CategoryListComponent() {
        super("category-list", "component/category-list/category-list.html", Lists.newArrayList(new IntegerAttribute("limit", null), new StringAttribute("title", null)));
    }

    @Override
    protected void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Optional<CategoryResponse> root = postCategoryWebService.findByPath("/");
        if (root.isPresent()) {
            CategoryQuery query = new CategoryQuery();
            query.parentId = root.get().id;
            query.limit = attributes.get("limit");
            QueryResponse<CategoryResponse> categories = postCategoryWebService.find(query);
            bindings.put("categories", categories);
            bindings.putAll(attributes);
            template().output(bindings, out);
        }
    }
}
