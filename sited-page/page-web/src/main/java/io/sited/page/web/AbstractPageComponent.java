package io.sited.page.web;

import com.google.common.collect.ImmutableList;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.ComponentAttribute;
import io.sited.template.TemplateComponent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public abstract class AbstractPageComponent extends TemplateComponent {
    public AbstractPageComponent(String name, String templatePath, List<ComponentAttribute<?>> componentAttributes) {
        super(name, templatePath, componentAttributes);
    }

    public AbstractPageComponent(String name, String templatePath) {
        super(name, templatePath, ImmutableList.of());
    }

    @Override
    public final void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        ComponentBindings componentBindings = new ComponentBindings(new HashMap<>(bindings));
        output(componentBindings, attributes, children, out);
    }

    protected abstract void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException;
}
