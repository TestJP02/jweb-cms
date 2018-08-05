package io.sited.web;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import io.sited.ApplicationException;
import io.sited.template.AbstractComponent;
import io.sited.template.Component;
import io.sited.template.ComponentAttribute;
import io.sited.template.Script;
import io.sited.template.StyleSheet;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;
import io.sited.template.TemplateException;

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

    protected Template template() {
        TemplateEngine templateEngine = templateEngine();
        String templatePath = templatePath();
        if (!Strings.isNullOrEmpty(theme)) {
            String themeTemplatePath = "theme/" + theme + '/' + templatePath;
            Optional<Template> template = templateEngine.template(themeTemplatePath, true);
            if (template.isPresent()) {
                return template.get();
            }
        }
        return templateEngine.template(templatePath, true).orElseThrow(() -> new TemplateException("missing template, path={}", templatePath));
    }
}
