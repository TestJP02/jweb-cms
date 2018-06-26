package io.sited.web.impl.exception;

import com.google.common.collect.Maps;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;
import io.sited.util.exception.Exceptions;
import io.sited.web.WebApplicationException;
import io.sited.web.WebOptions;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public abstract class AbstractWebApplicationExceptionMapper<T extends WebApplicationException> implements ExceptionMapper<T> {
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
            bindings.put("exception", exception);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                template.get().output(bindings, out);
                return Response.ok(out.toByteArray()).status(statusCode).type(MediaType.TEXT_HTML).build();
            } catch (IOException e) {
                e.initCause(e);
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
