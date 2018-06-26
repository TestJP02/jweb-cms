//package io.sited.page.web.service;
//
//import com.google.common.base.Charsets;
//import com.google.common.collect.Maps;
//import io.sited.http.Request;
//import io.sited.http.impl.ClientInfoImpl;
//import io.sited.resource.SingleResourceRepository;
//import io.sited.resource.StringResource;
//import io.sited.template.Template;
//import io.sited.template.TemplateEngine;
//import io.sited.template.impl.TemplateEngineImpl;
//import io.sited.util.i18n.MessageBundle;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.time.OffsetDateTime;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * @author chi
// */
//public class TimeIntervalFilterTest {
//    private Request request;
//    private MessageBundle messageBundle;
//
//    @Before
//    public void setup() {
//        request = mock(Request.class);
//        when(request.clientInfo()).thenReturn(new ClientInfoImpl(request));
//        when(request.require(Locale.class)).thenReturn(Locale.US);
//
//        messageBundle = Mockito.mock(MessageBundle.class);
//        Mockito.when(messageBundle.get("timeInterval.minuteBefore", Locale.US)).thenReturn(Optional.of("minute before"));
//        Mockito.when(messageBundle.get("timeInterval.minutesBefore", Locale.US)).thenReturn(Optional.of("minutes before"));
//        Mockito.when(messageBundle.get("timeInterval.hourBefore", Locale.US)).thenReturn(Optional.of("hour before"));
//        Mockito.when(messageBundle.get("timeInterval.hoursBefore", Locale.US)).thenReturn(Optional.of("hours before"));
//    }
//
//    @Test
//    public void testFilter() throws IOException {
//        TemplateEngine templateEngine = new TemplateEngineImpl().addRepository(new SingleResourceRepository(new StringResource("/test.html", template())));
//        templateEngine.addFilter("time-interval", new TimeIntervalFilter(messageBundle));
//        Template template = templateEngine.template("/test.html").orElseThrow(RuntimeException::new);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        Map<String, Object> bindings = Maps.newHashMap();
//        bindings.put("session", request);
//        long minute = 15;
//        bindings.put("time", OffsetDateTime.now().minusMinutes(minute));
//        template.output(bindings, out);
//        System.out.println(new String(out.toByteArray(), Charsets.UTF_8));
//        assertEquals("<!doctype html><html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\"><head><meta charset=\"UTF-8\"/><title></title></head><body><span href=\"/some/\">" + minute + " minutes before</span></body></html>", new String(out.toByteArray(), Charsets.UTF_8));
//    }
//
//
//    private String template() {
//        return "<!doctype html>\n"
//            + "<html lang=\"en-US\" xmlns:j=\"http://www.w3.org/1999/xhtml\">\n"
//            + "<head>\n"
//            + "    <meta charset=\"UTF-8\">\n"
//            + "    <title></title>\n"
//            + "</head>\n"
//            + "<body>\n"
//            + "<span href=\"/some/\" j:text=\"time-interval(time)\"></span>\n"
//            + "</body>\n"
//            + "</html>";
//    }
//
//}