package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPageComponent;
import app.jweb.page.web.PageBindings;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.StringAttribute;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class ImageComponent extends AbstractPageComponent {
    public ImageComponent() {
        super("image", "component/image/image.html", ImmutableList.of(
            new StringAttribute("caption", null),
            new StringAttribute("src", null),
            new StringAttribute("align", null)
        ));
    }

    @Override
    public void output(PageBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        template().output(bindings, out);
    }
}
