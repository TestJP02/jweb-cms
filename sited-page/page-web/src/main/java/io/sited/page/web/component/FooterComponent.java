package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class FooterComponent extends TemplateComponent {
    public FooterComponent() {
        super("footer", "component/page-footer/page-footer.html", ImmutableList.of(
            new ObjectAttribute<>("links", List.class, null),
            new StringAttribute("copyrights", null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = new HashMap<>();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);
        template().output(scopedBindings, out);
    }
}
