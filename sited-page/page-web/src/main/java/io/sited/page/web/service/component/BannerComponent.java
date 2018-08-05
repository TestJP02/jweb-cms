package io.sited.page.web.service.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.web.AbstractPageComponent;
import io.sited.template.Attributes;
import io.sited.page.web.Bindings;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author chi
 */
public class BannerComponent extends AbstractPageComponent {
    public BannerComponent() {
        super("banner", "component/page-banner/page-banner.html",
            ImmutableList.of(new ObjectAttribute<>("banners", List.class, null)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        template().output(bindings, out);
    }
}
