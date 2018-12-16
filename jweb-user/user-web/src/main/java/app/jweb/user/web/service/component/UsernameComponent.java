package app.jweb.user.web.service.component;

import app.jweb.user.web.service.UserCacheService;
import app.jweb.user.web.service.UserCacheView;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.StringAttribute;
import app.jweb.web.AbstractWebComponent;

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
