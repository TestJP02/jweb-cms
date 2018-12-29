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
public class ForgetPasswordFormComponent extends AbstractWebComponent {
    @Inject
    UserWebOptions userWebOptions;

    public ForgetPasswordFormComponent() {
        super("forget-password-form", "component/forget-password-form/forget-password-form.html", Lists.newArrayList());
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.put("userNameStrategy", userWebOptions.usernameStrategy);
        scopedBindings.putAll(bindings);
        template().output(scopedBindings, out);
    }
}
