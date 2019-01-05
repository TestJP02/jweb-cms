package app.jweb.page.web;

import app.jweb.page.api.page.PageResponse;
import app.jweb.page.web.service.TemplateCacheService;
import app.jweb.page.web.service.VariableCacheService;
import app.jweb.web.AbstractWebController;
import app.jweb.web.Template;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * @author chi
 */
public abstract class AbstractPageController extends AbstractWebController {
    @Inject
    TemplateCacheService templateService;

    @Inject
    VariableCacheService variableService;

    protected Response page(PageResponse page) {
        return page(page, ImmutableMap.of());
    }

    protected Response page(PageInfo post) {
        return page(post, ImmutableMap.of());
    }

    protected Response page(PageInfo post, Map<String, Object> bindings) {
        Map<String, Object> variables = variableService.variables();
        Map<String, Object> templateBindings = Maps.newHashMapWithExpectedSize(bindings.size() + variables.size() + 6);
        templateBindings.putAll(bindings);
        templateBindings.putAll(variables);
        templateBindings.put("app", appInfo);
        templateBindings.put("request", requestInfo);
        templateBindings.put("client", clientInfo);

        templateBindings.put("post", post);
        templateBindings.put("template", templateService.template(post.templatePath()));
        return Response.ok(new Template(post.templatePath(), templateBindings)).type(MediaType.TEXT_HTML).build();
    }

    protected Response page(PageResponse page, Map<String, Object> bindings) {
        Map<String, Object> variables = variableService.variables();
        Map<String, Object> templateBindings = Maps.newHashMapWithExpectedSize(bindings.size() + variables.size() + 6);
        templateBindings.putAll(bindings);
        templateBindings.putAll(variables);
        templateBindings.put("app", appInfo);
        templateBindings.put("request", requestInfo);
        templateBindings.put("client", clientInfo);
        templateBindings.put("page", page);
        templateBindings.put("template", templateService.template(page.templatePath));
        return Response.ok(new Template(page.templatePath, templateBindings)).type(MediaType.TEXT_HTML).build();
    }

    protected Response page(String title, String templatePath) {
        return page(title, null, templatePath);
    }

    protected Response page(String title, String description, String templatePath) {
        Map<String, Object> globalVariables = variableService.variables();
        Map<String, Object> bindings = Maps.newHashMapWithExpectedSize(globalVariables.size() + 6);
        bindings.putAll(globalVariables);

        PageResponse page = new PageResponse();
        page.title = title;
        page.templatePath = templatePath;
        bindings.put("page", page);
        bindings.put("app", appInfo);
        bindings.put("request", requestInfo);
        bindings.put("client", clientInfo);
        bindings.put("template", templateService.template(page.templatePath));
        return Response.ok(new Template(page.templatePath, bindings)).type(MediaType.TEXT_HTML).build();
    }
}
