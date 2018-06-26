package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.page.PageRelatedQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.page.web.service.CachedPageService;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.ObjectAttribute;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class PageRelatedListComponent extends TemplateComponent {
    @Inject
    CachedPageService cachedPageService;

    public PageRelatedListComponent() {
        super("page-related-list", "component/page-related-list/page-related-list.html", ImmutableList.of(
            new ObjectAttribute<>("page", PageResponse.class, null),
            new StringAttribute("title", null),
            new IntegerAttribute("limit", 5)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = new HashMap<>(bindings);
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);

        PageResponse page = (PageResponse) attribute("page").value(attributes);
        if (page == null) {
            page = (PageResponse) bindings.get("page");
        }
        if (page == null) {
            return;
        }

        PageRelatedQuery pageQuery = new PageRelatedQuery();
        pageQuery.limit = (Integer) attribute("limit").value(attributes);
        pageQuery.id = page.id;
        scopedBindings.put("pages", cachedPageService.findRelated(pageQuery));
        template().output(scopedBindings, out);
    }
}
