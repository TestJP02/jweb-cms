package io.sited.page.web.service.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageStatus;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.ComponentBindings;
import io.sited.page.web.service.PageService;
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
public class CategoryPageListComponent extends AbstractPageComponent {
    @Inject
    PageService pageService;
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public CategoryPageListComponent() {
        super("category-page-list", "component/category-page-list/category-page-list.html", ImmutableList.of(
            new StringAttribute("categoryId", null),
            new StringAttribute("title", null),
            new IntegerAttribute("page", null),
            new IntegerAttribute("limit", 20)));
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);

        PageQuery pageQuery = new PageQuery();
        String categoryId = attributes.get("categoryId");
        String path = bindings.request().path();
        String title = attributes.get("title");
        bindings.put("title", title);

        if (categoryId == null) {
            CategoryResponse category = (CategoryResponse) bindings.get("category");
            if (category != null) {
                pageQuery.categoryId = category.id;
                bindings.put("category", category);
                bindings.put("path", path == null ? category.path : path);
            }
        } else {
            CategoryResponse category = pageCategoryWebService.get(categoryId);
            pageQuery.categoryId = category.id;
            bindings.put("category", category);
            bindings.put("path", path == null ? category.path : path);
        }

        pageQuery.page = attributes.get("page");
        pageQuery.limit = attributes.get("limit");
        pageQuery.sortingField = "updatedTime";
        pageQuery.desc = true;
        pageQuery.status = PageStatus.ACTIVE;
        bindings.put("pages", pageService.find(pageQuery));
        bindings.put("display", 10);
        bindings.put("limit", pageQuery.limit);
        template().output(bindings, out);
    }
}
