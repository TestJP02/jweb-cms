package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageStatus;
import io.sited.page.web.service.CachedPageService;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.StringAttribute;
import io.sited.web.WebComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class PageListComponent extends WebComponent {
    @Inject
    CachedPageService pageManager;
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public PageListComponent() {
        super("page-list", "component/category-page-list/category-page-list.html", ImmutableList.of(
            new StringAttribute("categoryId", null),
            new StringAttribute("title", null),
            new IntegerAttribute("page", null),
            new IntegerAttribute("limit", 20)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = new HashMap<>();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);

        PageQuery pageQuery = new PageQuery();
        String categoryId = (String) attribute("categoryId").value(attributes);
        String path = request(bindings).path();
        String title = (String) attribute("title").value(attributes);
        scopedBindings.put("title", title);

        if (categoryId == null) {
            CategoryResponse category = (CategoryResponse) bindings.get("category");
            if (category != null) {
                pageQuery.categoryId = category.id;
                scopedBindings.put("category", category);
                scopedBindings.put("path", path == null ? category.path : path);
            }
        } else {
            CategoryResponse category = pageCategoryWebService.get(categoryId);
            pageQuery.categoryId = category.id;
            scopedBindings.put("category", category);
            scopedBindings.put("path", path == null ? category.path : path);
        }

        pageQuery.page = (Integer) attribute("page").value(attributes);
        pageQuery.limit = (Integer) attribute("limit").value(attributes);
        pageQuery.sortingField = "updatedTime";
        pageQuery.desc = true;
        pageQuery.status = PageStatus.ACTIVE;
        scopedBindings.put("pages", pageManager.find(pageQuery));
        scopedBindings.put("display", 10);
        scopedBindings.put("limit", pageQuery.limit);
        template().output(scopedBindings, out);
    }
}
