package io.sited.page.web;

import io.sited.page.api.page.PageComponentView;

import java.util.Map;

/**
 * @author chi
 */
public interface TemplateInfo {
    String templatePath();

    String type();

    Map<String, PageComponentView> components();
}
