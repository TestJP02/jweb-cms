package io.sited.page.web.service.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.category.CategoryNodeResponse;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.category.CategoryTreeQuery;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.Bindings;
import io.sited.template.Attributes;
import io.sited.template.BooleanAttribute;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.StringAttribute;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class HeaderComponent extends AbstractPageComponent {
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public HeaderComponent() {
        super("header", "component/header/header.html", ImmutableList.of(
            new StringAttribute("logoImageURL", null),
            new StringAttribute("logoText", "Sited"),
            new ObjectAttribute<>("links", List.class, null),
            new BooleanAttribute("searchEnabled", false),
            new StringAttribute("searchURL", null),
            new BooleanAttribute("userEnabled", false)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        List links = attributes.get("links");
        if (links == null || links.isEmpty()) {
            bindings.put("links", defaultLinks());
        }
        Boolean searchEnabled = attributes.get("searchEnabled");
        Boolean userEnabled = attributes.get("userEnabled");
        bindings.put("hasOperation", Boolean.TRUE.equals(searchEnabled) || Boolean.TRUE.equals(userEnabled));
        bindings.put("path", "/" + bindings.request().path());
        template().output(bindings, out);
    }

    private List<?> defaultLinks() {
        Optional<CategoryResponse> root = pageCategoryWebService.findByPath("/");
        List<Map<String, Object>> links = Lists.newArrayList();
        if (root.isPresent()) {
            CategoryTreeQuery query = new CategoryTreeQuery();
            query.parentId = root.get().id;
            query.depth = 2;
            List<CategoryNodeResponse> tree = pageCategoryWebService.tree(query);
            for (CategoryNodeResponse node : tree) {
                Map<String, Object> link = Maps.newHashMap();
                link.put("displayName", node.displayName);
                link.put("link", node.path);

                if (node.children != null && !node.children.isEmpty()) {
                    List<Map<String, Object>> subLinks = Lists.newArrayList();
                    for (CategoryNodeResponse child : node.children) {
                        Map<String, Object> subLink = Maps.newHashMap();
                        subLink.put("displayName", child.displayName);
                        subLink.put("link", child.path);
                        subLinks.add(subLink);
                    }
                    link.put("children", subLinks);
                }
                links.add(link);
            }
        }
        return links;
    }
}
