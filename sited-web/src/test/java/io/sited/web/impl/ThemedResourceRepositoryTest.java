package io.sited.web.impl;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import io.sited.ApplicationException;
import io.sited.resource.ClasspathResourceRepository;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;
import io.sited.web.impl.processor.HrefElementProcessor;
import io.sited.web.impl.processor.SrcElementProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class ThemedResourceRepositoryTest {
    TemplateEngine templateEngine;


    @BeforeEach
    public void setup() {
        templateEngine = new TemplateEngine();
        ClasspathResourceRepository repository = new ClasspathResourceRepository("web");
        templateEngine.addRepository(repository);
        templateEngine.addElementProcessor(new HrefElementProcessor(repository));
        templateEngine.addElementProcessor(new SrcElementProcessor(repository));
        templateEngine.addRepository(new ThemedResourceRepository("test", repository));
    }

    @Test
    void themedResources() throws IOException {
        Template template = templateEngine.template("template/test-theme.html").orElseThrow(() -> new ApplicationException("missing template"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.output(Maps.newHashMap(), out);
        assertEquals("<!doctype html><html lang=\"en-US\"><head><meta charset=\"UTF-8\"/><link rel=\"stylesheet\" href=\"/static/css/css.css\" type=\"text/css\"/></head><body>themed template</body></html>", new String(out.toByteArray(), Charsets.UTF_8));
    }
}