package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.TemplateComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class BreadcrumbComponent extends TemplateComponent {
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public BreadcrumbComponent() {
        super("breadcrumb", "component/page-breadcrumb/page-breadcrumb.html",
            ImmutableList.of(new ObjectAttribute<>("page", PageResponse.class, null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        PageResponse page = (PageResponse) attribute("page").value(attributes);
        if (page == null) {
            page = (PageResponse) bindings.get("page");
        }
        if (page != null && page.categoryId != null) {
            List<CategoryResponse> categories = Lists.newArrayList();
            categories.addAll(pageCategoryWebService.parents(page.categoryId));
            categories.add(pageCategoryWebService.get(page.categoryId));
            Map<String, Object> scopedBindings = Maps.newHashMapWithExpectedSize(bindings.size() + 1);
            scopedBindings.putAll(bindings);
            scopedBindings.put("categories", categories);
            template().output(scopedBindings, out);
        }
    }
}
