package app.jweb.template;

import java.lang.reflect.Type;
import java.util.Map;

public class ObjectAttribute<T> extends ComponentAttribute<T> {
    public ObjectAttribute(String name, Type type, T defaultValue) {
        super(name, type, defaultValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T value(Map<String, Object> attributes) {
        if (attributes.containsKey(name())) {
            return (T) attributes.get(name());
        }
        return defaultValue();
    }
}