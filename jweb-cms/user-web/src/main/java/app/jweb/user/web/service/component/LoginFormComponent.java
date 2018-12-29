package app.jweb.user.web.service.component;

import app.jweb.App;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.user.web.UserWebOptions;
import app.jweb.web.AbstractWebComponent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class LoginFormComponent extends AbstractWebComponent {
    @Inject
    App app;
    @Inject
    UserWebOptions userWebOptions;

    public LoginFormComponent() {
        super("login-form", "component/login-form/login-form.html", Lists.newArrayList());
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = Maps.newHashMap();
        bindings.put("baseURL", app.baseURL());
        bindings.put("showGOOGLELink", userWebOptions.google != null && userWebOptions.google.clientId != null);
        bindings.put("showFACEBOOKLink", userWebOptions.facebook != null && userWebOptions.facebook.clientId != null);
        scopedBindings.putAll(bindings);
        template().output(scopedBindings, out);
    }
}
