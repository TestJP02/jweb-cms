package io.sited.template.impl;

import com.google.common.collect.ImmutableList;
import io.sited.template.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class ComponentRegistry {
    private final Map<String, Component> components = new HashMap<>();

    public void put(String name, Component component) {
        components.put(name, component);
    }

    public Optional<Component> component(String name) {
        return Optional.ofNullable(components.get(name));
    }

    public List<Component> components() {
        return ImmutableList.copyOf(components.values());
    }
}
