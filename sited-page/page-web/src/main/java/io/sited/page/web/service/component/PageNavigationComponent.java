package io.sited.page.web.service.component;

import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageNavigationResponse;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.ComponentBindings;
import io.sited.page.web.PageInfo;
import io.sited.template.Attributes;
import io.sited.template.Children;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class PageNavigationComponent extends AbstractPageComponent {
    @Inject
    PageWebService pageWebService;

    public PageNavigationComponent() {
        super("page-navigation", "component/page-navigation/page-navigation.html");
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        PageInfo page = bindings.page();
        PageNavigationResponse navigation = pageWebService.navigation(page.id());
        bindings.put("prev", navigation.prev);
        bindings.put("next", navigation.next);
        template().output(bindings, out);
    }
}
