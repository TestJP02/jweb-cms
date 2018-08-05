package io.sited.page.web.service.component;

import com.google.common.collect.Lists;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.web.AbstractPageComponent;
import io.sited.template.Attributes;
import io.sited.page.web.Bindings;
import io.sited.page.web.PageInfo;
import io.sited.template.Children;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author chi
 */
public class BreadcrumbComponent extends AbstractPageComponent {
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public BreadcrumbComponent() {
        super("breadcrumb", "component/page-breadcrumb/page-breadcrumb.html");
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        PageInfo page = bindings.page();
        if (page.categoryId() != null) {
            List<CategoryResponse> categories = Lists.newArrayList();
            categories.addAll(pageCategoryWebService.parents(page.categoryId()));
            categories.add(pageCategoryWebService.get(page.categoryId()));
            bindings.put("categories", categories);
            template().output(bindings, out);
        }
    }
}
