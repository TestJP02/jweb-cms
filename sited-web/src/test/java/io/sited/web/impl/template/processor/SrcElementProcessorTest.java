package io.sited.web.impl.template.processor;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.resource.ClasspathResourceRepository;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;
import io.sited.template.TemplateFunctions;
import io.sited.template.TemplateResourceException;
import io.sited.util.i18n.MapMessageBundle;
import io.sited.web.WebOptions;
import io.sited.web.WebRoot;
import io.sited.web.impl.WebTemplateFunctions;
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
        WebRoot webRoot = new WebRoot(new ClasspathResourceRepository("web"));
        templateEngine.addElementProcessor(new SrcElementProcessor(webRoot));
        templateEngine.addRepository(webRoot);

        WebOptions webOptions = new WebOptions();
        webOptions.cdnBaseURLs = Lists.newArrayList("https://cdn.host");
        TemplateFunctions templateFunctions = new WebTemplateFunctions(new MapMessageBundle(Maps.newHashMap()), webOptions);
        templateEngine.addFunctions(templateFunctions);

        Template template = templateEngine.template("src-element-processor.html").orElseThrow(() -> new TemplateResourceException("missing template"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.output(Maps.newHashMap(), out);
        assertEquals("", new String(out.toByteArray(), Charsets.UTF_8));
    }
}