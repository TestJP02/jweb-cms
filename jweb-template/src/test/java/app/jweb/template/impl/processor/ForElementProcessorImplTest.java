package app.jweb.template.impl.processor;

import app.jweb.template.Template;
import app.jweb.template.TemplateEngine;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import app.jweb.resource.SingleResourceRepository;
import app.jweb.resource.StringResource;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author chi
 */
public class ForElementProcessorImplTest {
    @Test
    public void process() throws IOException {
        TemplateEngine templateEngine = new TemplateEngine().addRepository(new SingleResourceRepository(new StringResource("/test.html", template())));
        Template template = templateEngine.template("/test.html").orElseThrow(RuntimeException::new);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.output(Collections.singletonMap("items", Lists.newArrayList("1", "2")), out);
        assertEquals("<!doctype html><html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\"><head><meta charset=\"UTF-8\"/><title></title></head><body><div>1</div><div>2</div></body></html>", new String(out.toByteArray(), Charsets.UTF_8));
    }

    @Test
    public void priority() throws IOException {
        TemplateEngine templateEngine = new TemplateEngine().addRepository(new SingleResourceRepository(new StringResource("/test.html", templateWithIf())));
        Template template = templateEngine.template("/test.html").orElseThrow(RuntimeException::new);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.output(Collections.singletonMap("items", Lists.newArrayList("1", "2")), out);
        assertEquals("<!doctype html><html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\"><head><meta charset=\"UTF-8\"/><title></title></head><body><div>2</div></body></html>", new String(out.toByteArray(), Charsets.UTF_8));
    }

    private String template() {
        return "<!doctype html>\n"
            + "<html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\">\n"
            + "<head>\n"
            + "    <meta charset=\"UTF-8\">\n"
            + "    <title></title>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div j:for=\"item:items\">{{item}}</div>\n"
            + "</body>\n"
            + "</html>";
    }

    private String templateWithIf() {
        return "<!doctype html>\n"
            + "<html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\">\n"
            + "<head>\n"
            + "    <meta charset=\"UTF-8\">\n"
            + "    <title></title>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div j:for=\"item:items\" j:if=\"item=='2'\">{{item}}</div>\n"
            + "</body>\n"
            + "</html>";
    }
}