package io.sited.template;

import com.google.common.collect.ImmutableList;
import io.sited.ApplicationException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public abstract class AbstractComponent implements Component {
    private final String name;
    private final Map<String, ComponentAttribute<?>> attributes;

    public AbstractComponent(String name, List<ComponentAttribute<?>> attributes) {
        this.name = name;
        this.attributes = attributes.stream().collect(Collectors.toMap(ComponentAttribute::name, attribute -> attribute));
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<ComponentAttribute<?>> attributes() {
        return ImmutableList.copyOf(attributes.values());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ComponentAttribute<T> attribute(String attributeName) {
        ComponentAttribute<T> componentAttribute = (ComponentAttribute<T>) attributes.get(attributeName);
        if (componentAttribute == null) {
            throw new ApplicationException("missing attribute, name={}", attributeName);
        }
        return componentAttribute;
    }
}
