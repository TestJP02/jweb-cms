package io.sited.page.web.service.component;

import com.google.common.collect.Lists;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.category.CategoryQuery;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.ComponentBindings;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.StringAttribute;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

/**
 * @author chi
 */
public class CategoryListComponent extends AbstractPageComponent {
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public CategoryListComponent() {
        super("category-list", "component/category-list/category-list.html", Lists.newArrayList(new IntegerAttribute("limit", null), new StringAttribute("title", null)));
    }

    @Override
    protected void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Optional<CategoryResponse> root = pageCategoryWebService.findByPath("/");
        if (root.isPresent()) {
            CategoryQuery query = new CategoryQuery();
            query.parentId = root.get().id;
            query.limit = attributes.get("limit");
            QueryResponse<CategoryResponse> categories = pageCategoryWebService.find(query);
            bindings.put("categories", categories);
            bindings.putAll(attributes);
            template().output(bindings, out);
        }
    }
}
