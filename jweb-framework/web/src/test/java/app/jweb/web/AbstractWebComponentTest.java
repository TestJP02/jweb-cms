package app.jweb.web;

import app.jweb.resource.ClasspathResourceRepository;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.Component;
import app.jweb.template.TemplateEngine;
import app.jweb.template.TemplateException;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class AbstractWebComponentTest {
    TemplateEngine templateEngine;

    @BeforeEach
    public void setup() {
        templateEngine = new TemplateEngine();
        templateEngine.addRepository(new ClasspathResourceRepository("web"));
        templateEngine.addComponent(new TestComponent().setTemplateEngine(templateEngine).setTheme("test"));
    }

    @Test
    void themeComponent() throws IOException {
        Component component = templateEngine.component("test").orElseThrow(() -> new TemplateException("missing component, name=test"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        component.output(Maps.newHashMap(), new Attributes(Maps.newHashMap()), null, out);
        assertEquals("Themed Test", new String(out.toByteArray(), Charsets.UTF_8));
    }

    static class TestComponent extends AbstractWebComponent {
        public TestComponent() {
            super("test", "component/test/test.html");
        }

        @Override
        public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
            template().output(bindings, out);
        }
    }
}