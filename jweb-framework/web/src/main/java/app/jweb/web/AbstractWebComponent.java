package app.jweb.web;

import app.jweb.ApplicationException;
import app.jweb.template.AbstractComponent;
import app.jweb.template.Component;
import app.jweb.template.ComponentAttribute;
import app.jweb.template.Script;
import app.jweb.template.StyleSheet;
import app.jweb.template.Template;
import app.jweb.template.TemplateEngine;
import app.jweb.template.TemplateException;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @author chi
 */
public abstract class AbstractWebComponent extends AbstractComponent {
    private final String templatePath;
    TemplateEngine templateEngine;

    public AbstractWebComponent(String name, String templatePath) {
        this(name, templatePath, ImmutableList.of());
    }

    public AbstractWebComponent(String name, String templatePath, List<ComponentAttribute<?>> componentAttributes) {
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

    public AbstractWebComponent setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        return this;
    }

    protected Template template() {
        TemplateEngine templateEngine = templateEngine();
        String templatePath = templatePath();
        return templateEngine.template(templatePath, true).orElseThrow(() -> new TemplateException("missing template, path={}", templatePath));
    }
}
