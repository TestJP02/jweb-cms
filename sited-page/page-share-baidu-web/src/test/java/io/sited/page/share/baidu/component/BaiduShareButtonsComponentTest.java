package io.sited.page.share.baidu.component;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import io.sited.resource.ClasspathResourceRepository;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author chi
 */
public class BaiduShareButtonsComponentTest {
    private final TemplateEngine templateEngine = new TemplateEngine();

    @BeforeEach
    public void setup() {
        templateEngine.addRepository(new ClasspathResourceRepository("web"));
        BaiduShareButtonsComponent component = new BaiduShareButtonsComponent();
        component.setTemplateEngine(templateEngine);
        templateEngine.addComponent(component);
    }

    @Test
    public void output() throws IOException {
        Template template = templateEngine.template("template/test-baidu-share-buttons.html").orElseThrow(RuntimeException::new);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        template.output(Maps.newHashMap(), output);
        assertTrue(new String(output.toByteArray(), Charsets.UTF_8).contains("bdsharebuttonbox"));
    }
}