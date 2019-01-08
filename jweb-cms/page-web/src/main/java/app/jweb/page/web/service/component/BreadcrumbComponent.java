package app.jweb.page.web.service.component;

import app.jweb.page.api.PageCategoryWebService;
import app.jweb.page.api.category.CategoryResponse;
import app.jweb.page.web.AbstractPageComponent;
import app.jweb.page.web.PageBindings;
import app.jweb.page.web.PageInfo;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author chi
 */
public class BreadcrumbComponent extends AbstractPageComponent {
    @Inject
    PageCategoryWebService postCategoryWebService;

    public BreadcrumbComponent() {
        super("breadcrumb", "component/post-breadcrumb/post-breadcrumb.html");
    }

    @Override
    public void output(PageBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        PageInfo page = bindings.page();
        if (page.categoryId != null) {
            List<CategoryResponse> categories = Lists.newArrayList();
            categories.addAll(postCategoryWebService.parents(page.categoryId));
            categories.add(postCategoryWebService.get(page.categoryId));
            bindings.put("categories", categories);
            template().output(bindings, out);
        }
    }
}
