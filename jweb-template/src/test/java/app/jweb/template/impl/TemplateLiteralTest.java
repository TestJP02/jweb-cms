package app.jweb.template.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class TemplateLiteralTest {
    @Test
    void parse() {
        TemplateLiteral parser = new TemplateLiteral("hello {{world}}");
        assertEquals("\"hello \"+world", parser.expr());
    }

    @Test
    void quote() {
        TemplateLiteral parser = new TemplateLiteral("\"hello\" {{world}}");
        assertEquals("\"\\\"hello\\\" \"+world", parser.expr());
    }
}