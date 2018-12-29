package app.jweb.page.web;

import app.jweb.page.api.component.PostComponentView;

import java.util.Map;

/**
 * @author chi
 */
public interface TemplateInfo {
    String templatePath();

    String type();

    Map<String, PostComponentView> components();
}
