package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.ObjectAttribute;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author chi
 */
public class BannerComponent extends AbstractPostComponent {
    public BannerComponent() {
        super("banner", "component/post-banner/post-banner.html",
            ImmutableList.of(new ObjectAttribute<>("banners", List.class, null)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        template().output(bindings, out);
    }
}
