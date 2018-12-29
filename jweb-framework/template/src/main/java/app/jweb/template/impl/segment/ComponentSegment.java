package app.jweb.template.impl.segment;

import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.Component;
import app.jweb.template.Expression;
import app.jweb.template.impl.Segment;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class ComponentSegment implements Segment {
    private final Component component;
    private final Children children;
    private final Map<String, Expression> attributes;

    public ComponentSegment(Component component, Children children, Map<String, Expression> attributes) {
        this.component = component;
        this.children = children;
        this.attributes = attributes;
    }

    @Override
    public void output(Map<String, Object> bindings, OutputStream outputStream) throws IOException {
        Map<String, Object> attributeValues = Maps.newHashMapWithExpectedSize(this.attributes.size());
        this.attributes.forEach((key, expression) -> attributeValues.put(key, expression.eval(bindings)));
        component.output(bindings, new Attributes(attributeValues), children, outputStream);
    }
}
