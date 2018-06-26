package io.sited.web;

import io.sited.template.ComponentAttribute;
import io.sited.template.TemplateComponent;
import io.sited.template.TemplateException;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public abstract class WebComponent extends TemplateComponent {
    protected WebComponent(String name, String templatePath, List<ComponentAttribute<?>> componentAttributes) {
        super(name, templatePath, componentAttributes);
    }

    public AppInfo appInfo(Map<String, Object> bindings) {
        AppInfo app = (AppInfo) bindings.get("app");
        if (app == null) {
            throw new TemplateException("missing template binding, name=app");
        }
        return app;
    }

    public UserInfo userInfo(Map<String, Object> bindings) {
        UserInfo user = (UserInfo) bindings.get("user");
        if (user == null) {
            throw new TemplateException("missing template binding, name=user");
        }
        return user;
    }

    public ClientInfo clientInfo(Map<String, Object> bindings) {
        ClientInfo clientInfo = (ClientInfo) bindings.get("client");
        if (clientInfo == null) {
            throw new TemplateException("missing template binding, name=client");
        }
        return clientInfo;
    }


    public RequestInfo request(Map<String, Object> bindings) {
        RequestInfo requestInfo = (RequestInfo) bindings.get("request");
        if (requestInfo == null) {
            throw new TemplateException("missing template binding, name=request");
        }
        return requestInfo;
    }
}
