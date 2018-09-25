package app.jweb.service;

import app.jweb.AbstractModule;
import app.jweb.Binder;
import app.jweb.Configurable;
import app.jweb.service.impl.ServiceConfigImpl;
import app.jweb.service.impl.controller.HealthCheckService;
import app.jweb.service.impl.controller.HealthCheckServiceImpl;
import app.jweb.service.impl.exception.BadRequestExceptionMapper;
import app.jweb.service.impl.exception.DefaultExceptionMapper;
import app.jweb.service.impl.exception.ForbiddenExceptionMapper;
import app.jweb.service.impl.exception.NotAuthorizedExceptionMapper;
import app.jweb.service.impl.exception.NotFoundExceptionMapper;
import app.jweb.service.impl.exception.ValidationExceptionMapper;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.validation.ValidationFeature;

/**
 * @author chi
 */
public final class ServiceModule extends AbstractModule implements Configurable<ServiceConfig> {
    private final JerseyClient client = new JerseyClientBuilder()
        .register(JacksonFeature.class)
        .register(ValidationFeature.class)
        .build();

    @Override
    protected void configure() {
        app().register(ForbiddenExceptionMapper.class);
        app().register(NotAuthorizedExceptionMapper.class);
        app().register(NotFoundExceptionMapper.class);
        app().register(ValidationExceptionMapper.class);
        app().register(BadRequestExceptionMapper.class);
        app().register(DefaultExceptionMapper.class);

        module(ServiceModule.class)
            .service(HealthCheckService.class, HealthCheckServiceImpl.class);
    }

    @Override
    public ServiceConfig configurator(AbstractModule module, Binder binder) {
        return new ServiceConfigImpl(module, binder, client, app());
    }
}
