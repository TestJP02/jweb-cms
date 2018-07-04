package io.sited.template;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author chi
 */
public class Attributes implements Map<String, Object> {
    private final Map<String, Object> attributeValues;

    public Attributes(Map<String, Object> values) {
        this.attributeValues = values;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) attributeValues.get(name);
    }

    @Override
    public int size() {
        return attributeValues.size();
    }

    @Override
    public boolean isEmpty() {
        return attributeValues.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return attributeValues.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return attributeValues.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return attributeValues.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return attributeValues.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return attributeValues.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        attributeValues.putAll(m);
    }

    @Override
    public void clear() {
        attributeValues.clear();
    }

    @Override
    public Set<String> keySet() {
        return attributeValues.keySet();
    }

    @Override
    public Collection<Object> values() {
        return attributeValues.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return attributeValues.entrySet();
    }
}
