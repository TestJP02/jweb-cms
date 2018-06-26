package io.sited.template.impl.processor;

import com.google.common.base.Charsets;
import io.sited.resource.SingleResourceRepository;
import io.sited.resource.StringResource;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author chi
 */
public class ClassElementProcessorImplTest {
    @Test
    public void process() throws IOException {
        TemplateEngine templateEngine = new TemplateEngine().addRepository(new SingleResourceRepository(new StringResource("/test.html", template())));
        Template template = templateEngine.template("test.html").orElseThrow(RuntimeException::new);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.output(Collections.singletonMap("active", true), out);
        assertEquals("<!doctype html><html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\"><head><meta charset=\"UTF-8\"/><title></title></head><body><div class=\"active 1\"></div></body></html>", new String(out.toByteArray(), Charsets.UTF_8));
    }

    private String template() {
        return "<!doctype html>\n"
            + "<html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\">\n"
            + "<head>\n"
            + "    <meta charset=\"UTF-8\">\n"
            + "    <title></title>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div class=\"1\" j:class=\"active?'active':''\"></div>\n"
            + "</body>\n"
            + "</html>";
    }
}