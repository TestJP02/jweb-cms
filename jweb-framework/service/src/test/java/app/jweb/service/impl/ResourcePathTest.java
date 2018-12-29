package app.jweb.service.impl;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class ResourcePathTest {
    @Test
    public void parse() {
        ResourcePath resourcePath = new ResourcePath("/api/user/{id:\\d+}");
        List<String> variableNames = resourcePath.variableNames;
        assertEquals(1, variableNames.size());
        assertEquals("id", variableNames.get(0));
    }

    @Test
    public void format() {
        ResourcePath resourcePath = new ResourcePath("/api/user/{id:\\d+}");
        assertEquals("/api/user/1", resourcePath.format(Collections.singletonMap("id", "1")));
    }

    @Test
    public void formatArray() {
        ResourcePath resourcePath = new ResourcePath("/api/user/{id}");
        assertEquals("/api/user/1", resourcePath.format("1"));
    }
}