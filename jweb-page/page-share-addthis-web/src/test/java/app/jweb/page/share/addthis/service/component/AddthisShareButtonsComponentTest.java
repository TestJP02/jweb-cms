package app.jweb.page.share.addthis.service.component;

import app.jweb.page.share.addthis.AddthisOptions;
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
public class AddthisShareButtonsComponentTest {
    private final TemplateEngine templateEngine = new TemplateEngine();

    @BeforeEach
    public void setup() {
        templateEngine.addRepository(new ClasspathResourceRepository("web"));
        AddthisOptions options = new AddthisOptions();
        options.id = "xxx";
        AddthisShareButtonsComponent component = new AddthisShareButtonsComponent(options);
        component.setTemplateEngine(templateEngine);
        templateEngine.addComponent(component);
    }

    @Test
    public void output() throws IOException {
        Template template = templateEngine.template("template/test-addthis-share-buttons.html").orElseThrow(RuntimeException::new);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        template.output(Maps.newHashMap(), output);
        assertEquals("<!doctype html><html lang=\"en-US\" xmlns:j=\"\"><head></head><body><div class=\"component addthis-share-buttons\"><div class=\"component__body\"><div class=\"addthis_inline_share_toolbox\"></div><script async=\"null\" src=\"//s7.addthis.com/js/300/addthis_widget.js#pubid=xxx\" type=\"text/javascript\"></script></div></div></body></html>", new String(output.toByteArray(), Charsets.UTF_8));
    }
}