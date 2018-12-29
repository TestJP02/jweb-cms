package app.jweb.service.impl;

import app.jweb.AbstractModule;
import app.jweb.App;
import app.jweb.ApplicationException;
import app.jweb.Binder;
import com.google.common.base.Strings;
import app.jweb.service.ServiceConfig;
import org.glassfish.jersey.client.JerseyClient;

import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author chi
 */
public class ServiceConfigImpl implements ServiceConfig {
    private final AbstractModule module;
    private final Binder binder;
    private final App app;
    private final JerseyClient client;

    public ServiceConfigImpl(AbstractModule module, Binder binder, JerseyClient client, App app) {
        this.module = module;
        this.binder = binder;
        this.app = app;
        this.client = client;
    }

    @Override
    public <T> ServiceConfig service(Class<T> serviceClass, String serviceURL) {
        if (!isValidServiceURL(serviceURL)) {
            throw new ApplicationException("invalid service entry URL, service={}, serviceURL=", serviceClass.getCanonicalName(), serviceURL);
        }
        ProxyResourceClientBuilder<T> builder = new ProxyResourceClientBuilder<>(serviceClass, module, new ResourceClient(serviceURL, client));
        T service = builder.build();
        binder.bind(serviceClass).toInstance(service);
        return this;
    }

    @Override
    public <T> ServiceConfig service(Class<T> serviceClass, Class<? extends T> serviceImplClass) {
        binder.bind(serviceClass).to(serviceImplClass);
        if (app.isAPIEnabled()) {
            app.register(new ServiceDelegateBuilder<>(serviceClass).build());
        }
        return this;
    }

    @Override
    public ServiceConfig bindExceptionMapper(Class<ExceptionMapper<? extends Throwable>> exceptionMapperClass) {
        app.register(exceptionMapperClass);
        return this;
    }

    private boolean isValidServiceURL(String serviceURL) {
        return !Strings.isNullOrEmpty(serviceURL) && (serviceURL.startsWith("https://") || serviceURL.startsWith("http://"));
    }
}
