package io.sited.page.web;

import com.google.common.collect.ImmutableList;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.ComponentAttribute;
import io.sited.web.AbstractWebComponent;

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
        Bindings componentBindings = new Bindings(new HashMap<>(bindings));
        output(componentBindings, attributes, children, out);
    }

    protected abstract void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException;
}
