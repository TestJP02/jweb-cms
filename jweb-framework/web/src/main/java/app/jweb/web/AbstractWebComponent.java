package app.jweb.web;

import app.jweb.ApplicationException;
import app.jweb.template.AbstractComponent;
import app.jweb.template.Component;
import app.jweb.template.ComponentAttribute;
import app.jweb.template.Script;
import app.jweb.template.StyleSheet;
import app.jweb.template.TemplateEngine;
import app.jweb.template.TemplateException;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public abstract class AbstractWebComponent extends AbstractComponent {
    private final String templatePath;
    TemplateEngine templateEngine;
    String theme;

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


    public AbstractWebComponent setTheme(String theme) {
        this.theme = theme;
        return this;
    }

    public AbstractWebComponent setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        return this;
    }

    protected app.jweb.template.Template template() {
        TemplateEngine templateEngine = templateEngine();
        String templatePath = templatePath();
        if (!Strings.isNullOrEmpty(theme)) {
            String themeTemplatePath = "theme/" + theme + '/' + templatePath;
            Optional<app.jweb.template.Template> template = templateEngine.template(themeTemplatePath, true);
            if (template.isPresent()) {
                return template.get();
            }
        }
        return templateEngine.template(templatePath, true).orElseThrow(() -> new TemplateException("missing template, path={}", templatePath));
    }
}
