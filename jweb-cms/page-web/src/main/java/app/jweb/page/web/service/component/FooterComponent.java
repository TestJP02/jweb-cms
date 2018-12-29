package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.post.api.PostCategoryWebService;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.ObjectAttribute;
import app.jweb.template.StringAttribute;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class FooterComponent extends AbstractPostComponent {
    @Inject
    PostCategoryWebService postCategoryWebService;

    public FooterComponent() {
        super("footer", "component/footer/footer.html", ImmutableList.of(
            new ObjectAttribute<>("links", List.class, null),
            new StringAttribute("copyrights", null)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);

        List links = attributes.get("links");
        if (links == null || links.isEmpty()) {
            bindings.put("links", defaultLinks());
        }

        String copyrights = attributes.get("copyrights");
        if (Strings.isNullOrEmpty(copyrights)) {
            bindings.put("copyrights", "ⒸSited 2018");
        }

        template().output(bindings, out);
    }


    private List<?> defaultLinks() {
        Optional<CategoryResponse> root = postCategoryWebService.findByPath("/");
        List<Map<String, Object>> links = Lists.newArrayList();
        if (root.isPresent()) {
            List<CategoryResponse> children = postCategoryWebService.children(root.get().id);
            for (CategoryResponse category : children) {
                Map<String, Object> link = Maps.newHashMap();
                link.put("displayName", category.displayName);
                link.put("link", category.path);
                links.add(link);
            }
        }
        return links;
    }
}
