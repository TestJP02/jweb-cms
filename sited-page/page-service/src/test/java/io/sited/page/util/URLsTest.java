package io.sited.page.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
public class URLsTest {
    @Test
    public void normalize() {
        assertEquals("/some/a-b", URLs.normalize("/some/a---b"));
    }
}