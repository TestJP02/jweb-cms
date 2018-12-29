package app.jweb.page.tracking.baidu.service.component;

import app.jweb.page.tracking.baidu.service.BaiduTrackingScriptService;
import app.jweb.page.tracking.baidu.service.processor.BaiduTrackingElementProcessor;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import app.jweb.resource.ClasspathResourceRepository;
import app.jweb.template.Template;
import app.jweb.template.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class BaiduTrackingComponentTest {
    private final TemplateEngine templateEngine = new TemplateEngine();

    @BeforeEach
    public void setup() {
        templateEngine.addRepository(new ClasspathResourceRepository("web"));
        templateEngine.addElementProcessor(new BaiduTrackingElementProcessor());
        BaiduTrackingComponent component = new BaiduTrackingComponent();
        component.baiduTrackingScriptService = new BaiduTrackingScriptService("f8e272739ffd4ffa36f98dde1a74cbf7");
        component.setTemplateEngine(templateEngine);
        templateEngine.addComponent(component);
    }

    @Test
    public void output() throws IOException {
        Template template = templateEngine.template("template/test-baidu-tracking.html").orElseThrow(RuntimeException::new);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        template.output(Maps.newHashMap(), output);
        assertEquals("<!doctype html><html lang=\"en-US\"><head></head><body><script type=\"text/javascript\">var _hmt = _hmt || [];(function() {  var hm = document.createElement(\"script\"); hm.src = \"https://hm.baidu.com/hm.js?f8e272739ffd4ffa36f98dde1a74cbf7\"; var s = document.getElementsByTagName(\"script\")[0]; s.parentNode.insertBefore(hm, s);})();</script></body></html>", new String(output.toByteArray(), Charsets.UTF_8));
    }
}