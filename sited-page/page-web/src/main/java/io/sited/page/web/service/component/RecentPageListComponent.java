package io.sited.page.web.service.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.page.PageQuery;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.Bindings;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.StringAttribute;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class RecentPageListComponent extends AbstractPageComponent {
    @Inject
    PageWebService pageWebService;
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public RecentPageListComponent() {
        super("recent-page-list", "component/recent-page-list/recent-page-list.html", ImmutableList.of(
            new StringAttribute("title", null),
            new StringAttribute("categoryId", null),
            new IntegerAttribute("page", 1),
            new IntegerAttribute("limit", 20)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        PageQuery query = new PageQuery();
        query.page = attributes.get("page");
        query.limit = attributes.get("limit");
        query.sortingField = "updatedTime";
        query.desc = true;
        String categoryId = attributes.get("categoryId");
        bindings.put("title", attributes.get("title"));
        if (categoryId == null) {
            CategoryResponse category = (CategoryResponse) bindings.get("category");
            if (category != null) {
                query.categoryId = category.id;
                bindings.put("category", category);
            }
        } else {
            CategoryResponse category = pageCategoryWebService.get(categoryId);
            query.categoryId = category.id;
            bindings.put("category", category);
        }
        bindings.put("pages", pageWebService.find(query));
        template().output(bindings, out);
    }
}
