package app.jweb.page.web;

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

    protected Response post(PostInfo post) {
        return post(post, ImmutableMap.of());
    }

    protected Response post(PostInfo post, Map<String, Object> bindings) {
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
}
