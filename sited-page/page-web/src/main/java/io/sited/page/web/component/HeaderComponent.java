package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import io.sited.template.BooleanAttribute;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.StringAttribute;
import io.sited.web.WebComponent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class HeaderComponent extends WebComponent {
    public HeaderComponent() {
        super("header", "component/page-header/page-header.html", ImmutableList.of(
            new StringAttribute("logoImageURL", null),
            new StringAttribute("logoText", ""),
            new ObjectAttribute<>("links", List.class, null),
            new BooleanAttribute("searchEnabled", false),
            new StringAttribute("searchURL", null),
            new BooleanAttribute("userEnabled", false)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        ;
        Map<String, Object> scopedBindings = new HashMap<>();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);
        Boolean searchEnabled = (Boolean) attributes.get("searchEnabled");
        Boolean userEnabled = (Boolean) attributes.get("userEnabled");
        scopedBindings.put("hasOperation", Boolean.TRUE.equals(searchEnabled) || Boolean.TRUE.equals(userEnabled));
        scopedBindings.put("path", "/" + request(bindings).path());
        template().output(scopedBindings, out);
    }
}
