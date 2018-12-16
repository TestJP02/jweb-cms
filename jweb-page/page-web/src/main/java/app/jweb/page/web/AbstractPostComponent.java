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
public abstract class AbstractPostComponent extends AbstractWebComponent {
    public AbstractPostComponent(String name, String templatePath, List<ComponentAttribute<?>> componentAttributes) {
        super(name, templatePath, componentAttributes);
    }

    public AbstractPostComponent(String name, String templatePath) {
        super(name, templatePath, ImmutableList.of());
    }

    @Override
    public final void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Bindings componentBindings = new Bindings(new HashMap<>(bindings));
        output(componentBindings, attributes, children, out);
    }

    protected abstract void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException;
}
