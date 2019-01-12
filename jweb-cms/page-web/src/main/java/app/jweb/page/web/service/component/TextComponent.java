package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPageComponent;
import app.jweb.page.web.PageBindings;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.IntegerAttribute;
import app.jweb.template.StringAttribute;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class TextComponent extends AbstractPageComponent {
    public TextComponent() {
        super("text", "component/text/text.html", ImmutableList.of(
            new IntegerAttribute("fontSize", 14),
            new StringAttribute("fontSizeUnit", "px"),
            new IntegerAttribute("lineHeight", 14),
            new StringAttribute("lineHeightUnit", "px")
        ));
    }

    @Override
    public void output(PageBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        template().output(bindings, out);
    }
}
