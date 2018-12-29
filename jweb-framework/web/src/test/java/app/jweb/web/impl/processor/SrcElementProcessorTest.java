package app.jweb.web.impl.processor;

import app.jweb.resource.ClasspathResourceRepository;
import app.jweb.template.Template;
import app.jweb.template.TemplateEngine;
import app.jweb.template.TemplateFunctions;
import app.jweb.template.TemplateResourceException;
import app.jweb.util.i18n.MapMessageBundle;
import app.jweb.web.WebRoot;
import app.jweb.web.impl.WebTemplateFunctions;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class SrcElementProcessorTest {
    @Test
    void process() throws IOException {
        TemplateEngine templateEngine = new TemplateEngine();
        WebRoot webRoot = new WebRoot();
        webRoot.add(new ClasspathResourceRepository("web"));
        templateEngine.addElementProcessor(new SrcElementProcessor(Lists.newArrayList("https://cdn.host/"), webRoot, false));
        templateEngine.addRepository(webRoot);

        TemplateFunctions templateFunctions = new WebTemplateFunctions(new MapMessageBundle(Maps.newHashMap()));
        templateEngine.addFunctions(templateFunctions);

        Template template = templateEngine.template("template/src-element-processor.html").orElseThrow(() -> new TemplateResourceException("missing template"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.output(Maps.newHashMap(), out);
        assertEquals("<!doctype html><html lang=\"en-US\"><head><script src=\"https://cdn.host/static/js/js.js\" type=\"text/javascript\"></script></head><body></body></html>", new String(out.toByteArray(), Charsets.UTF_8));
    }

    @Test
    void inline() throws IOException {
        TemplateEngine templateEngine = new TemplateEngine();
        WebRoot webRoot = new WebRoot();
        webRoot.add(new ClasspathResourceRepository("web"));
        templateEngine.addElementProcessor(new SrcElementProcessor(Lists.newArrayList("https://cdn.host/"), webRoot, true));
        templateEngine.addRepository(webRoot);

        TemplateFunctions templateFunctions = new WebTemplateFunctions(new MapMessageBundle(Maps.newHashMap()));
        templateEngine.addFunctions(templateFunctions);

        Template template = templateEngine.template("template/src-element-processor.html").orElseThrow(() -> new TemplateResourceException("missing template"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.output(Maps.newHashMap(), out);
        assertEquals("<!doctype html><html lang=\"en-US\"><head><script type=\"text/javascript\">(function() {\n" +
            "})();</script></head><body></body></html>", new String(out.toByteArray(), Charsets.UTF_8));
    }
}