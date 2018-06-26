package io.sited.page.meta.web.service.processor;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
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
        TemplateEngine templateEngine = new TemplateEngine().addRepository(new SingleResourceRepository(new StringResource("/test.html", template())));
        templateEngine.addElementProcessor(new OgMetaElementProcessor());

        Template template = templateEngine.template("/test.html").orElseThrow(RuntimeException::new);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Map<String, Object> page = Maps.newHashMap();
        page.put("title", "title");
        page.put("imageURL", "http://localhost/image.jpg");
        page.put("description", "description");
        page.put("fields", Collections.singletonMap("type", "recipe"));
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("page", page);
        bindings.put("request", request);
        bindings.put("app", app);
        template.output(bindings, out);
        assertEquals("<!doctype html><html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\"><head><meta property=\"og:type\" content=\"recipe\"/><meta property=\"og:locale\" content=\"en\"/><meta property=\"og:url\" content=\"http://localhost/test\"/><meta property=\"og:title\" content=\"title\"/><meta property=\"og:description\" content=\"description\"/><meta property=\"og:image\" content=\"http://localhost/image.jpg\"/></head><body></body></html>", new String(out.toByteArray(), Charsets.UTF_8));
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