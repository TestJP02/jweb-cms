package io.sited.web;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.sited.template.TemplateEngine;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public abstract class AbstractWebController {
    @Inject
    protected AppInfo appInfo;

    @Inject
    protected RequestInfo requestInfo;

    @Inject
    protected ClientInfo clientInfo;

    @Inject
    WebOptions webOptions;

    @Inject
    TemplateEngine templateEngine;

    protected Response template(String templatePath, Map<String, Object> bindings) {
        Map<String, Object> templateBindings = Maps.newHashMapWithExpectedSize(bindings.size() + 4);
        templateBindings.putAll(bindings);
        templateBindings.put("app", appInfo);
        templateBindings.put("request", requestInfo);
        templateBindings.put("client", clientInfo);
        return Response.ok(new Template(themeTemplatePath(templatePath), templateBindings)).type(MediaType.TEXT_HTML).build();
    }

    protected String themeTemplatePath(String templatePath) {
        if (!Strings.isNullOrEmpty(webOptions.theme)) {
            String themeTemplatePath = "theme/" + webOptions.theme + '/' + templatePath;
            Optional<io.sited.template.Template> template = templateEngine.template(themeTemplatePath);
            if (template.isPresent()) {
                return themeTemplatePath;
            }
        }
        return templatePath;
    }
}
