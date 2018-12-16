package app.jweb.post.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class URLsTest {
    @Test
    void normalize() {
        assertEquals("/some/a-b", URLs.normalize("/some/a---b"));
    }

    @Test
    void path() {
        assertEquals("this-is-a-test", URLs.segment("this is a  test"));
    }
}