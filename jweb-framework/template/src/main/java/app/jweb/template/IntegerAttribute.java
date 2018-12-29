package app.jweb.template;

import java.util.Map;

public class IntegerAttribute extends ComponentAttribute<Integer> {
    public IntegerAttribute(String name, Integer defaultValue) {
        super(name, Integer.class, defaultValue);
    }

    @Override
    public Integer value(Map<String, Object> attributes) {
        if (attributes.containsKey(name())) {
            return (Integer) attributes.get(name());
        }
        return defaultValue();
    }
}