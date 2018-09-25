package app.jweb.web.impl.exception;

import app.jweb.template.Template;
import app.jweb.template.TemplateEngine;
import app.jweb.util.exception.Exceptions;
import app.jweb.web.WebException;
import app.jweb.web.WebOptions;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public abstract class AbstractWebApplicationExceptionMapper<T extends WebException> implements ExceptionMapper<T> {
    private final Response.Status statusCode;
    @Inject
    WebOptions webOptions;
    @Inject
    TemplateEngine templateEngine;

    protected AbstractWebApplicationExceptionMapper(Response.Status statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public Response toResponse(T exception) {
        Optional<Template> template = templateEngine.template(templatePath());
        if (template.isPresent()) {
            Map<String, Object> bindings = Maps.newHashMap();
            bindings.put("e", exception);
            bindings.put("app", exception.app);
            bindings.put("request", exception.request);
            bindings.put("client", exception.client);
            bindings.put("page", null);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                template.get().output(bindings, out);
                return Response.ok(out.toByteArray()).status(statusCode).type(MediaType.TEXT_HTML).build();
            } catch (Throwable e) {
                e.addSuppressed(exception);
                return printStackTrace(e);
            }
        }
        return printStackTrace(exception);
    }

    protected Response printStackTrace(Throwable e) {
        return Response.ok(Exceptions.stackTrace(e)).status(statusCode).type(MediaType.TEXT_PLAIN).build();
    }

    protected String templatePath() {
        String templatePath = webOptions.errorPages.get(statusCode.getStatusCode());
        if (templatePath == null) {
            templatePath = webOptions.errorPages.get(500);
        }
        return templatePath;
    }
}
