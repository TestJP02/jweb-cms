package io.sited.web;

import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public interface RequestInfo {
    String path();

    String method();

    Optional<String> queryParam(String name);

    Map<String, String> headers();

    Map<String, String> cookies();

    String uri();
}
