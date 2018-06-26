package io.sited.web.impl;

import io.sited.template.TemplateEngine;
import io.sited.web.Template;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author chi
 */
@Priority(Priorities.ENTITY_CODER - 100)
public class TemplateMessageBodyWriter implements MessageBodyWriter<Object> {
    @Inject
    TemplateEngine templateEngine;

    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Template.class.isAssignableFrom(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeTo(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        Template view = (Template) o;
        io.sited.template.Template template = templateEngine.template(view.path).orElseThrow(() -> new NotFoundException(String.format("missing template, path=%s", view.path)));
        template.output(view.bindings, entityStream);

        if (!httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML);
        }
    }
}
