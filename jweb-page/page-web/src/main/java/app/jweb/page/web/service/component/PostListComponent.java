package app.jweb.page.web.service.component;

import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.ObjectAttribute;
import app.jweb.template.StringAttribute;
import app.jweb.web.AbstractWebComponent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class PostListComponent extends AbstractWebComponent {
    public PostListComponent() {
        super("post-list", "component/post-list/post-list.html", Lists.newArrayList(
            new ObjectAttribute<>("posts", Iterable.class, null),
            new StringAttribute("class", null)
        ));
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> componentBindings = Maps.newHashMap();
        componentBindings.putAll(attributes);
        componentBindings.putAll(bindings);
        template().output(componentBindings, out);
    }
}
