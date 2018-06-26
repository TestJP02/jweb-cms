package io.sited.template.impl;

import io.sited.resource.StringResource;
import io.sited.template.Node;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class HTMLParserTest {
    @Test
    void parse() throws IOException {
        HTMLParser htmlParser = new HTMLParser(new StringResource("test.html", "<!doctype html><html><div>some</div><!-- some --></html>"));
        List<Node> elements = htmlParser.parse();
        assertEquals(2, elements.size());
    }
}