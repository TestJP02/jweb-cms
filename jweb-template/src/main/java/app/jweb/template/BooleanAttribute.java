package app.jweb.template;

import java.util.Map;

public class BooleanAttribute extends ComponentAttribute<Boolean> {
    public BooleanAttribute(String name, Boolean defaultValue) {
        super(name, Boolean.class, defaultValue);
    }

    @Override
    public Boolean value(Map<String, Object> attributes) {
        if (attributes.containsKey(name())) {
            return (Boolean) attributes.get(name());
        }
        return defaultValue();
    }
}