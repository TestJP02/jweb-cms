package io.sited.email.service;

import io.sited.template.TemplateEngine;

/**
 * @author chi
 */
public class EmailTemplateEngineManager {
    private final TemplateEngine templateEngine;

    public EmailTemplateEngineManager(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public TemplateEngine get() {
        return this.templateEngine;
    }
}
