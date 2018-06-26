package io.sited.page.admin.service;

import java.util.UUID;
import java.util.function.Function;

/**
 * @author chi
 */
public class PathProviderImpl implements Function<String, String> {
    @Override
    public String apply(String s) {
        return UUID.randomUUID().toString();
    }
}
