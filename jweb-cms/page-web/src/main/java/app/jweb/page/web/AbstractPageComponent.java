package app.jweb.page.web;

import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.ComponentAttribute;
import app.jweb.web.AbstractWebComponent;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public abstract class AbstractPageComponent extends AbstractWebComponent {
    public AbstractPageComponent(String name, String templatePath, List<ComponentAttribute<?>> componentAttributes) {
        super(name, templatePath, componentAttributes);
    }

    public AbstractPageComponent(String name, String templatePath) {
        super(name, templatePath, ImmutableList.of());
    }

    @Override
    public final void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        PageBindings pageBindings = new PageBindings(new HashMap<>(bindings));
        output(pageBindings, attributes, children, out);
    }

    protected abstract void output(PageBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException;
}
