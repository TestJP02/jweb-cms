package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageNavigationResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.TemplateComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class PageNavigationComponent extends TemplateComponent {
    @Inject
    PageWebService pageWebService;

    public PageNavigationComponent() {
        super("page-navigation", "component/page-navigation/page-navigation.html", ImmutableList.of(
            new ObjectAttribute<>("page", PageResponse.class, null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.putAll(bindings);

        PageResponse page = (PageResponse) attribute("page").value(attributes);
        if (page == null) {
            page = (PageResponse) bindings.get("page");
        }
        if (page == null) {
            return;
        }
        PageNavigationResponse navigation = pageWebService.navigation(page.id);
        scopedBindings.put("prev", navigation.prev);
        scopedBindings.put("next", navigation.next);
        template().output(scopedBindings, out);
    }
}
