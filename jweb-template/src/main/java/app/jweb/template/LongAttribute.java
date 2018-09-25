package app.jweb.template;

import java.util.Map;

public class LongAttribute extends ComponentAttribute<Long> {
    public LongAttribute(String name, Long defaultValue) {
        super(name, Long.class, defaultValue);
    }

    @Override
    public Long value(Map<String, Object> attributes) {
        Object value = attributes.get(name());
        if (attributes.containsKey(name())) {
            return (Long) value;
        }
        return defaultValue();
    }
}