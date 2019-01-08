package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPageComponent;
import app.jweb.page.web.PageBindings;
import app.jweb.template.Attributes;
import app.jweb.template.BooleanAttribute;
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
public class HeaderComponent extends AbstractPageComponent {
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
    public void output(PageBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        Boolean searchEnabled = attributes.get("searchEnabled");
        Boolean userEnabled = attributes.get("userEnabled");
        bindings.put("hasOperation", Boolean.TRUE.equals(searchEnabled) || Boolean.TRUE.equals(userEnabled));
        bindings.put("path", "/" + bindings.request().path());
        template().output(bindings, out);
    }
}
