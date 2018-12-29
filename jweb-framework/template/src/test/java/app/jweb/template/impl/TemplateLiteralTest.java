package app.jweb.template.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class TemplateLiteralTest {
    @Test
    void parse() {
        TemplateText parser = new TemplateText("hello {{world}}");
        assertEquals("\"hello \"+world", parser.expr());
    }

    @Test
    void quote() {
        TemplateText parser = new TemplateText("\"hello\" {{world}}");
        assertEquals("\"\\\"hello\\\" \"+world", parser.expr());
    }

    @Test
    void tokens() {
        String text = "The following list are {{items.size()}} best {{map.fields().get(\"key\")}} gift ideas for {{map.fields().get(\"key\")}}";
        TemplateText literal = new TemplateText(text);
        assertEquals(6, literal.tokens().size());
    }
}