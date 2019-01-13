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
import java.util.HashMap;

/**
 * @author chi
 */
public class HeroComponent extends AbstractPageComponent {
    public HeroComponent() {
        super("hero", "component/hero/hero.html", ImmutableList.of(
            new StringAttribute("title", null),
            new StringAttribute("subtitle", null),
            new ObjectAttribute<>("btn1", HashMap.class, null),
            new ObjectAttribute<>("btn2", HashMap.class, null),
            new StringAttribute("background", null),
            new StringAttribute("clip", null)
            
        ));
    }

    @Override
    public void output(PageBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        template().output(bindings, out);
    }
}
