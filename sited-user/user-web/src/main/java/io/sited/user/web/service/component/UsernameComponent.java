package io.sited.user.web.service.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.StringAttribute;
import io.sited.web.AbstractWebComponent;
import io.sited.user.web.service.UserCacheService;
import io.sited.user.web.service.UserCacheView;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class UsernameComponent extends AbstractWebComponent {
    @Inject
    UserCacheService userCacheService;

    public UsernameComponent() {
        super("username", "component/user-username/user-username.html", Lists.newArrayList(
            new StringAttribute("userId", null)
        ));
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        String userId = attributes.get("userId");
        if (userId != null) {
            UserCacheView user = userCacheService.get(userId);
            Map<String, Object> componentBindings = Maps.newHashMap();
            componentBindings.put("user", user);
            componentBindings.putAll(attributes);
            template().output(componentBindings, out);
        }
    }
}
