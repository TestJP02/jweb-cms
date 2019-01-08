package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPageComponent;
import app.jweb.page.web.PageBindings;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.ObjectAttribute;
import app.jweb.template.StringAttribute;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author chi
 */
public class FooterComponent extends AbstractPageComponent {
    public FooterComponent() {
        super("footer", "component/footer/footer.html", ImmutableList.of(
            new ObjectAttribute<>("links", List.class, null),
            new StringAttribute("copyrights", null)));
    }

    @Override
    public void output(PageBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        template().output(bindings, out);
    }
}
