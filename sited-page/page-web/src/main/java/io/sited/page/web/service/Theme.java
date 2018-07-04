package io.sited.page.web.service;

import io.sited.ApplicationException;
import io.sited.template.Script;
import io.sited.template.StyleSheet;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;

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
