package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.page.web.PostInfo;
import app.jweb.post.api.PostCategoryWebService;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.category.CategoryNodeResponse;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.post.api.post.PostQuery;
import app.jweb.post.api.post.PostResponse;
import app.jweb.template.Attributes;
import app.jweb.template.BooleanAttribute;
import app.jweb.template.Children;
import app.jweb.template.StringAttribute;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

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
public class CategoryTreeComponent extends AbstractPostComponent {
    @Inject
    PostCategoryWebService postCategoryWebService;
    @Inject
    PostWebService postWebService;

    public CategoryTreeComponent() {
        super("category-tree", "component/post-category-tree/post-category-tree.html", ImmutableList.of(
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

        String selectedPageId = bindings.post().id();
        String categoryId = attributes.get("categoryId");
        Boolean pageEnabled = attributes.get("pageEnabled");
        String title = attributes.get("title");

        List<String> categoryIds = Lists.newArrayList(postCategoryWebService.parents(selectedCategoryId).stream().map(category -> category.id).collect(Collectors.toList()));
        categoryIds.add(selectedCategoryId);

        if (categoryId != null) {
            int index = categoryIds.indexOf(categoryId);
            if (index > 0) {
                categoryIds = categoryIds.subList(index, categoryIds.size());
            } else if (index < 0) {
                categoryIds = Lists.newArrayList(categoryId);
            }
        }

//        List<CategoryNodeResponse> nodes = pageCategoryWebService.subTree(categoryIds);
        List<CategoryNodeResponse> nodes = null;
        //TODO
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
            PostInfo page = bindings.post();
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
                PostQuery postQuery = new PostQuery();
                postQuery.userId = category.id;
                postQuery.sortingField = "title";
                QueryResponse<PostResponse> posts = postWebService.find(postQuery);
                b.append("<ul>");
                for (PostResponse post : posts) {
                    if (Objects.equals(post.id, selectedPageId)) {
                        b.append("<li class=\"category-tree__item category-tree__item--active\"><a href=\"");
                    } else {
                        b.append("<li class=\"category-tree__item\"><a href=\"");
                    }
                    b.append(post.path);
                    b.append("\">");
                    b.append(post.title);
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
