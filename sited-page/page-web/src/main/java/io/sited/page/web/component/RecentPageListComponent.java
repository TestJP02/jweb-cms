package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.page.PageQuery;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class RecentPageListComponent extends TemplateComponent {
    @Inject
    PageWebService pageWebService;
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public RecentPageListComponent() {
        super("recent-page-list", "component/page-recent-link-list/page-recent-link-list.html", ImmutableList.of(
            new StringAttribute("title", null),
            new StringAttribute("categoryId", null),
            new IntegerAttribute("page", 1),
            new IntegerAttribute("limit", 20)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);
        PageQuery query = new PageQuery();
        query.page = (Integer) attribute("page").value(attributes);
        query.limit = (Integer) attribute("limit").value(attributes);
        query.sortingField = "updatedTime";
        query.desc = true;
        String categoryId = (String) attribute("categoryId").value(attributes);
        scopedBindings.put("title", attribute("title").value(attributes));
        if (categoryId == null) {
            CategoryResponse category = (CategoryResponse) bindings.get("category");
            if (category != null) {
                query.categoryId = category.id;
                scopedBindings.put("category", category);
            }
        } else {
            CategoryResponse category = pageCategoryWebService.get(categoryId);
            query.categoryId = category.id;
            scopedBindings.put("category", category);
        }
        scopedBindings.put("pages", pageWebService.find(query));
        template().output(scopedBindings, out);
    }
}
