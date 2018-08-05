package io.sited.web;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import io.sited.resource.ClasspathResourceRepository;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.Component;
import io.sited.template.TemplateEngine;
import io.sited.template.TemplateException;
import io.sited.web.impl.Theme;
import io.sited.web.impl.component.ThemeCSSComponent;
import io.sited.web.impl.component.ThemeScriptComponent;
import io.sited.web.impl.processor.ThemeProcessor;
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
        templateEngine.addElementProcessor(new ThemeProcessor());

        Theme theme = new Theme("test", templateEngine);
        templateEngine.addComponent(new ThemeScriptComponent(theme));
        templateEngine.addComponent(new ThemeCSSComponent(theme));
        templateEngine.addComponent(new TestComponent().setTemplateEngine(templateEngine).setTheme(theme.name()));
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