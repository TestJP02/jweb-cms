package io.sited.template;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.sited.ApplicationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class TemplateComponent extends AbstractComponent {
    private final String templatePath;
    TemplateEngine templateEngine;

    public TemplateComponent(String name, String templatePath) {
        this(name, templatePath, ImmutableList.of());
    }

    public TemplateComponent(String name, String templatePath, List<ComponentAttribute<?>> componentAttributes) {
        super(name, componentAttributes);
        this.templatePath = templatePath;
    }

    protected Template template() {
        if (templateEngine == null) {
            throw new ApplicationException("missing template engine");
        }
        return templateEngine.template(templatePath, true).orElseThrow(() -> new ApplicationException("missing template, path={}", templatePath));
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopeBindings = Maps.newHashMapWithExpectedSize(bindings.size() + 1);
        scopeBindings.putAll(bindings);
        scopeBindings.putAll(attributes);
        template().output(scopeBindings, out);
    }

    @Override
    public List<Component> refs() {
        return template().refs();
    }

    @Override
    public List<Script> scripts() {
        return template().scripts();
    }

    @Override
    public List<StyleSheet> styles() {
        return template().styles();
    }
}
