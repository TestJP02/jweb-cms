package io.sited.web;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author chi
 */
public class Template {
    public static Template of(String path, Map<String, Object> bindings) {
        return new Template(path, bindings);
    }

    public static Template of(String path) {
        return new Template(path);
    }

    public final String path;
    public final Map<String, Object> bindings;

    public Template(String path, Map<String, Object> bindings) {
        this.path = path;
        this.bindings = bindings;
    }

    public Template(String path) {
        this(path, ImmutableMap.of());
    }
}
