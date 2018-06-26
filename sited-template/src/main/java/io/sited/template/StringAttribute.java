package io.sited.template;

import java.util.Map;

public class StringAttribute extends ComponentAttribute<String> {
    public StringAttribute(String name, String defaultValue) {
        super(name, String.class, defaultValue);
    }

    @Override
    public String value(Map<String, Object> attributes) {
        if (attributes.containsKey(name())) {
            return (String) attributes.get(name());
        }
        return defaultValue();
    }
}