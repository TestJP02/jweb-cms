package app.jweb.web.impl.processor;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import app.jweb.resource.ClasspathResourceRepository;
import app.jweb.template.Template;
import app.jweb.template.TemplateEngine;
import app.jweb.template.TemplateResourceException;
import app.jweb.web.WebRoot;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class HrefElementProcessorTest {
    @Test
    void process() throws IOException {
        TemplateEngine templateEngine = new TemplateEngine();
        WebRoot webRoot = new WebRoot();
        webRoot.add(new ClasspathResourceRepository("web"));
        templateEngine.addElementProcessor(new HrefElementProcessor(Lists.newArrayList("https://cdn.host/"), webRoot));
        templateEngine.addRepository(webRoot);

        Template template = templateEngine.template("template/href-element-processor.html").orElseThrow(() -> new TemplateResourceException("missing template"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.output(Maps.newHashMap(), out);
        assertEquals("<!doctype html><html lang=\"en-US\"><head><link rel=\"stylesheet\" href=\"https://cdn.host/static/css/css.css\" type=\"text/css\"/></head><body></body></html>", new String(out.toByteArray(), Charsets.UTF_8));
    }
}