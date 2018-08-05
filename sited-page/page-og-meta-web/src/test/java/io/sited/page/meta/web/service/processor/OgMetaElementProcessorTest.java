package io.sited.page.meta.web.service.processor;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import io.sited.page.meta.web.service.component.OgMetaComponent;
import io.sited.page.web.PageInfo;
import io.sited.resource.SingleResourceRepository;
import io.sited.resource.StringResource;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;
import io.sited.web.AppInfo;
import io.sited.web.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author chi
 */
public class OgMetaElementProcessorTest {
    private RequestInfo request;
    private AppInfo app;

    @BeforeEach
    public void setup() {
        request = mock(RequestInfo.class);
        when(request.uri()).thenReturn("http://localhost/test");

        app = mock(AppInfo.class);
        when(app.language()).thenReturn("en");
    }

    @Test
    public void process() throws IOException {
        TemplateEngine templateEngine = new TemplateEngine().addRepository(new SingleResourceRepository(new StringResource("template/test.html", template())));
        templateEngine.addElementProcessor(new OgMetaElementProcessor());
        templateEngine.addComponent(new OgMetaComponent());

        Template template = templateEngine.template("template/test.html").orElseThrow(RuntimeException::new);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PageInfo pageInfo = PageInfo.builder()
            .setTitle("title")
            .setDescription("description")
            .setPath("")
            .setImageURL("http://localhost/image.jpg")
            .setFields(Collections.singletonMap("type", "recipe"))
            .build();

        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("page", pageInfo);
        bindings.put("request", request);
        bindings.put("app", app);
        template.output(bindings, out);
        assertEquals("<!doctype html><html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\"><head><meta property=\"og:title\" content=\"title\"/><meta property=\"og:type\" content=\"recipe\"/><meta property=\"og:image\" content=\"http://localhost/image.jpg\"/><meta property=\"og:url\" content=\"http://localhost/test\"/><meta property=\"og:description\" content=\"description\"/></head><body></body></html>", new String(out.toByteArray(), Charsets.UTF_8));
    }

    private String template() {
        return "<!doctype html>\n"
            + "<html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\">\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";
    }
}