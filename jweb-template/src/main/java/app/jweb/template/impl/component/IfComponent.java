package app.jweb.template.impl.component;

import app.jweb.template.AbstractComponent;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.ObjectAttribute;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class IfComponent extends AbstractComponent {
    public IfComponent() {
        super("if", Lists.newArrayList(new ObjectAttribute<>("condition", boolean.class, false)));
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Boolean condition = attributes.get("condition");

        if (Boolean.TRUE.equals(condition)) {
            children.output(bindings, out);
        }
    }
}
