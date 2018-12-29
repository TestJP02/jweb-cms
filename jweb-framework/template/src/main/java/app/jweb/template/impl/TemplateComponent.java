package app.jweb.template.impl;

import app.jweb.ApplicationException;
import app.jweb.template.AbstractComponent;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.Component;
import app.jweb.template.ComponentAttribute;
import app.jweb.template.Script;
import app.jweb.template.StyleSheet;
import app.jweb.template.Template;
import app.jweb.template.TemplateEngine;
import app.jweb.template.TemplateException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

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

    protected String templatePath() {
        if (templatePath == null) {
            throw new ApplicationException("missing template path, name={}", name());
        }
        return templatePath;
    }

    protected TemplateEngine templateEngine() {
        if (templateEngine == null) {
            throw new ApplicationException("missing template engine, name={}", name());
        }
        return templateEngine;
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

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> componentBindings = Maps.newHashMap();
        componentBindings.putAll(bindings);
        componentBindings.putAll(attributes);
        template().output(componentBindings, out);
    }

    public TemplateComponent setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        return this;
    }

    protected Template template() {
        return templateEngine().template(templatePath, true).orElseThrow(() -> new TemplateException("missing template, path={}", templatePath));
    }
}
