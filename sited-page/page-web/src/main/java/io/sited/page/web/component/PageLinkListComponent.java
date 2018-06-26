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
import io.sited.web.WebComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class PageLinkListComponent extends WebComponent {
    @Inject
    PageWebService pageWebService;
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public PageLinkListComponent() {
        super("page-link-list", "component/category-page-link-list/category-page-link-list.html",
            ImmutableList.of(
                new StringAttribute("title", null),
                new StringAttribute("categoryId", null),
                new IntegerAttribute("page", null),
                new IntegerAttribute("limit", 10)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Integer page = (Integer) attribute("page").value(attributes);
        if (page == null) {
            page = request(bindings).queryParam("page").map(Integer::parseInt).orElse(1);
        }

        String categoryId = (String) attribute("categoryId").value(attributes);
        CategoryResponse category = (CategoryResponse) bindings.get("category");
        if (categoryId == null && category != null) {
            categoryId = category.id;
        }
        if (categoryId != null && category == null) {
            category = pageCategoryWebService.get(categoryId);
        }

        String title = (String) attribute("title").value(attributes);
        if (title == null && category != null) {
            title = category.displayName;
        }

        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.putAll(bindings);
        scopedBindings.put("category", category);
        scopedBindings.put("title", title);

        PageQuery query = new PageQuery();
        query.page = page;
        query.limit = (Integer) attribute("limit").value(attributes);
        query.sortingField = "updatedTime";
        query.categoryId = categoryId;
        query.desc = true;
        scopedBindings.put("pages", pageWebService.find(query));
        template().output(scopedBindings, out);
    }
}
