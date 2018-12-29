package app.jweb.web.impl;

import app.jweb.ApplicationException;
import app.jweb.template.Script;
import app.jweb.template.StyleSheet;
import app.jweb.template.Template;
import app.jweb.template.TemplateEngine;

import java.util.List;

/**
 * @author chi
 */
public class Theme {
    private final String name;
    private final TemplateEngine templateEngine;

    public Theme(String name, TemplateEngine templateEngine) {
        this.name = name;
        this.templateEngine = templateEngine;
    }

    public String name() {
        return name;
    }

    public List<StyleSheet> styles() {
        return template().styles();
    }

    public List<Script> scripts() {
        return template().scripts();
    }

    private Template template() {
        String templatePath = "theme/" + name + "/theme.html";
        return templateEngine.template(templatePath).orElseThrow(() -> new ApplicationException("missing theme file, path={}", templatePath));
    }
}
