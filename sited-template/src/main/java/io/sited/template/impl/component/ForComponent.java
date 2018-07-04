package io.sited.template.impl.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.template.AbstractComponent;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.StringAttribute;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class ForComponent extends AbstractComponent {
    public ForComponent() {
        super("for", Lists.newArrayList(new StringAttribute("item", "item"), new StringAttribute("index", "$index"), new ObjectAttribute<>("items", Iterable.class, null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Iterable<?> items = attributes.get("items");
        String item = attributes.get("item");
        String index = attributes.get("index");

        if (items != null) {
            int i = 0;
            Map<String, Object> scopedBindings = Maps.newHashMap();
            scopedBindings.putAll(bindings);
            for (Object value : items) {
                scopedBindings.put(item, value);
                scopedBindings.put(index, i++);
                children.output(scopedBindings, out);
            }
        }
    }
}
