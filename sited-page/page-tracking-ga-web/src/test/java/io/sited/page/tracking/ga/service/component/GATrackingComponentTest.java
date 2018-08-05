package io.sited.page.tracking.ga.service.component;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import io.sited.page.tracking.ga.service.GAStatisticsScriptService;
import io.sited.page.tracking.ga.service.processor.GATrackingElementProcessor;
import io.sited.resource.ClasspathResourceRepository;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */

class GATrackingComponentTest {
    private final TemplateEngine templateEngine = new TemplateEngine();

    @BeforeEach
    public void setup() {
        templateEngine.addRepository(new ClasspathResourceRepository("web"));
        templateEngine.addElementProcessor(new GATrackingElementProcessor());
        GATrackingComponent component = new GATrackingComponent();
        component.gaStatisticsScriptService = new GAStatisticsScriptService("f8e272739ffd4ffa36f98dde1a74cbf7");
        component.setTemplateEngine(templateEngine);
        templateEngine.addComponent(component);
    }

    @Test
    public void output() throws IOException {
        Template template = templateEngine.template("template/test-ga-tracking.html").orElseThrow(RuntimeException::new);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        template.output(Maps.newHashMap(), output);
        assertEquals("<!doctype html><html lang=\"en-US\"><head></head><body><script type=\"text/javascript\">(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');ga('create', 'f8e272739ffd4ffa36f98dde1a74cbf7', 'auto');ga('send', 'pageview');</script></body></html>", new String(output.toByteArray(), Charsets.UTF_8));
    }
}