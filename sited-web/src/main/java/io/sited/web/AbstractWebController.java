package io.sited.web;

import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

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

    protected Response template(String templatePath, Map<String, Object> bindings) {
        Map<String, Object> templateBindings = Maps.newHashMapWithExpectedSize(bindings.size() + 4);
        templateBindings.putAll(bindings);
        templateBindings.put("app", appInfo);
        templateBindings.put("request", requestInfo);
        templateBindings.put("client", clientInfo);
        return Response.ok(new Template(templatePath, templateBindings)).type(MediaType.TEXT_HTML).build();
    }
}
