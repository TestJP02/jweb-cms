package io.sited.user.web.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.template.Children;
import io.sited.template.TemplateComponent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class ResetPasswordFormComponent extends TemplateComponent {
    public ResetPasswordFormComponent() {
        super("reset-password-form", "component/reset-password-form/reset-password-form.html", Lists.newArrayList());
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.putAll(bindings);
        template().output(scopedBindings, out);
    }
}
