package io.sited.user.web.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.template.BooleanAttribute;
import io.sited.template.Children;
import io.sited.template.ComponentAttribute;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;
import io.sited.user.web.service.UserCachedService;
import io.sited.user.web.service.UserCachedView;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class UsernameComponent extends TemplateComponent {
    @Inject
    UserCachedService userCachedService;

    public UsernameComponent() {
        super("username", "component/user-username/user-username.html", Lists.newArrayList(
            new StringAttribute("userId", null),
            new BooleanAttribute("imageEnabled", false),
            new BooleanAttribute("linkEnabled", false)
        ));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.putAll(bindings);
        for (ComponentAttribute<?> componentAttribute : attributes()) {
            scopedBindings.put(componentAttribute.name(), componentAttribute.value(attributes));
        }
        String userId = (String) attribute("userId").value(attributes);
        UserCachedView user = userCachedService.get(userId);
        scopedBindings.put("user", user);
        template().output(scopedBindings, out);
    }
}
