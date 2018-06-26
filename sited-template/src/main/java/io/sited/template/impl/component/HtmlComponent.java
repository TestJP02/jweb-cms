package io.sited.template.impl.component;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import io.sited.template.AbstractComponent;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class HtmlComponent extends AbstractComponent {
    public HtmlComponent() {
        super("html", Lists.newArrayList(new ObjectAttribute<>("content", Object.class, null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Object content = attribute("content").value(attributes);
        if (content == null) {
            children.output(bindings, out);
        } else {
            out.write(content.toString().getBytes(Charsets.UTF_8));
        }
    }
}
