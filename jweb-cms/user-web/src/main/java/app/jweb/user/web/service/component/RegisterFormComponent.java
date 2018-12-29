package app.jweb.user.web.service.component;

import app.jweb.user.web.UserWebOptions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.web.AbstractWebComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class RegisterFormComponent extends AbstractWebComponent {
    @Inject
    UserWebOptions userWebOptions;

    public RegisterFormComponent() {
        super("register-form", "component/register-form/register-form.html", Lists.newArrayList());
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.put("userNameStrategy", userWebOptions.usernameStrategy);
        scopedBindings.put("validationRules", userWebOptions.validationRules);
        scopedBindings.putAll(bindings);
        template().output(scopedBindings, out);
    }
}
