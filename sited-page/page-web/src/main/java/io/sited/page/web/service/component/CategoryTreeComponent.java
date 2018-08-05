package io.sited.page.web.service.component;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.category.CategoryNodeResponse;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.Bindings;
import io.sited.page.web.PageInfo;
import io.sited.template.Attributes;
import io.sited.template.BooleanAttribute;
import io.sited.template.Children;
import io.sited.template.StringAttribute;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class CategoryTreeComponent extends AbstractPageComponent {
    @Inject
    PageCategoryWebService pageCategoryWebService;
    @Inject
    PageWebService pageWebService;

    public CategoryTreeComponent() {
        super("category-tree", "component/page-category-tree/page-category-tree.html", ImmutableList.of(
            new StringAttribute("categoryId", null),
            new StringAttribute("title", null),
            new BooleanAttribute("pageEnabled", false)));

    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        String selectedCategoryId = selectedCategoryId(bindings);
        if (Strings.isNullOrEmpty(selectedCategoryId)) {
            return;
        }

        String selectedPageId = bindings.page().id();
        String categoryId = attributes.get("categoryId");
        Boolean pageEnabled = attributes.get("pageEnabled");
        String title = attributes.get("title");

        List<String> categoryIds = Lists.newArrayList(pageCategoryWebService.parents(selectedCategoryId).stream().map(category -> category.id).collect(Collectors.toList()));
        categoryIds.add(selectedCategoryId);

        if (categoryId != null) {
            int index = categoryIds.indexOf(categoryId);
            if (index > 0) {
                categoryIds = categoryIds.subList(index, categoryIds.size());
            } else if (index < 0) {
                categoryIds = Lists.newArrayList(categoryId);
            }
        }

        List<CategoryNodeResponse> nodes = pageCategoryWebService.subTree(categoryIds);
        if (nodes.isEmpty()) {
            return;
        }

        StringBuilder b = new StringBuilder();
        build(nodes, categoryIds, selectedPageId, pageEnabled, b);
        bindings.put("treeHtml", b.toString());
        bindings.put("title", title);
        template().output(bindings, out);
    }

    private String selectedCategoryId(Bindings bindings) {
        String selectedCategoryId = null;
        CategoryResponse category = category(bindings);
        if (category != null) {
            selectedCategoryId = category.id;
        }
        if (selectedCategoryId == null) {
            PageInfo page = bindings.page();
            if (page != null) {
                selectedCategoryId = page.categoryId();
            }
        }
        return selectedCategoryId;
    }


    private CategoryResponse category(Map<String, Object> bindings) {
        return (CategoryResponse) bindings.get("category");
    }

    private void build(List<CategoryNodeResponse> nodes, List<String> selectedCategoryIds, String selectedPageId, Boolean pageEnabled, StringBuilder b) {
        b.append("<ul class=\"category-tree__list\">");
        for (CategoryNodeResponse category : nodes) {
            int found = selectedCategoryIds.indexOf(category.id);

            if (found >= 0) {
                b.append("<li class=\"category-tree__item category-tree__item--active\"><a href=\"");
            } else {
                b.append("<li class=\"category-tree__item\"><a href=\"");
            }
            b.append(category.path);
            b.append("\">");
            b.append(category.displayName);

            if (found == selectedCategoryIds.size() - 1 && Boolean.TRUE.equals(pageEnabled)) {
                PageQuery pageQuery = new PageQuery();
                pageQuery.userId = category.id;
                pageQuery.sortingField = "title";
                QueryResponse<PageResponse> pages = pageWebService.find(pageQuery);
                b.append("<ul>");
                for (PageResponse page : pages) {
                    if (Objects.equals(page.id, selectedPageId)) {
                        b.append("<li class=\"category-tree__item category-tree__item--active\"><a href=\"");
                    } else {
                        b.append("<li class=\"category-tree__item\"><a href=\"");
                    }
                    b.append(page.path);
                    b.append("\">");
                    b.append(page.title);
                    b.append("</a></li>");
                }
                b.append("</ul>");

            } else if (found >= 0) {
                build(category.children, selectedCategoryIds, selectedPageId, pageEnabled, b);
            }

            b.append("</a></li>");
        }
        b.append("</ul>");
    }
}
