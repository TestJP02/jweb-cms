package io.sited.web.impl;

import io.sited.App;
import io.sited.Binder;
import io.sited.resource.ResourceRepository;
import io.sited.template.Component;
import io.sited.template.ElementProcessor;
import io.sited.template.TemplateEngine;
import io.sited.web.AbstractWebComponent;
import io.sited.web.WebConfig;
import io.sited.web.WebOptions;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.WriterInterceptor;


/**
 * @author chi
 */
public class WebConfigImpl implements WebConfig {
    private final Binder binder;
    private final WebOptions webOptions;
    private final TemplateEngine templateEngine;
    private final App app;

    public WebConfigImpl(Binder binder, WebOptions webOptions, TemplateEngine templateEngine, App app) {
        this.binder = binder;
        this.webOptions = webOptions;
        this.templateEngine = templateEngine;
        this.app = app;
    }

    @Override
    public <T> WebConfig controller(Class<T> controllerClass) {
        binder.bind(controllerClass).in(RequestScoped.class);
        app.register(controllerClass);
        return this;
    }

    @Override
    public WebConfig bindWriterInterceptor(WriterInterceptor interceptor) {
        app.register(interceptor);
        return this;
    }

    @Override
    public WebConfig bindReaderInterceptor(ReaderInterceptor interceptor) {
        app.register(interceptor);
        return this;
    }

    @Override
    public WebConfig bindRequestFilter(ContainerRequestFilter filter) {
        app.register(filter);
        return this;
    }

    @Override
    public WebConfig bindResponseFilter(ContainerResponseFilter filter) {
        app.register(filter);
        return this;
    }

    @Override
    public <T> WebConfig bindMessageBodyReader(MessageBodyReader<T> messageBodyReader) {
        app.register(messageBodyReader);
        return this;
    }

    @Override
    public <T> WebConfig bindMessageBodyWriter(MessageBodyWriter<T> messageBodyWriter) {
        app.register(messageBodyWriter);
        return this;
    }

    @Override
    public <T> WebConfig bind(Class<T> contextClass, Class<? extends Provider<T>> providerClass) {
        binder.bind(contextClass).toProvider(providerClass).in(RequestScoped.class);
        return this;
    }

    @Override
    public <T> WebConfig bind(Class<T> contextClass, Provider<T> provider) {
        binder.bind(contextClass).toProvider(provider).in(RequestScoped.class);
        return this;
    }

    @Override
    public WebConfig bindExceptionMapper(ExceptionMapper<? extends Throwable> exceptionMapper) {
        app.register(exceptionMapper);
        return this;
    }

    @Override
    public WebConfig addComponent(Component component) {
        if (component instanceof AbstractWebComponent) {
            AbstractWebComponent abstractWebComponent = (AbstractWebComponent) component;
            abstractWebComponent.setTemplateEngine(templateEngine);
            abstractWebComponent.setTheme(webOptions.theme);
        }
        templateEngine.addComponent(component);
        return this;
    }

    @Override
    public WebConfig addRepository(ResourceRepository repository) {
        templateEngine.addRepository(repository);
        return this;
    }

    @Override
    public WebConfig addElementProcessor(ElementProcessor processor) {
        templateEngine.addElementProcessor(processor);
        return this;
    }
}
