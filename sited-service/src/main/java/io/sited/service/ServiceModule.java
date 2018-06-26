package io.sited.service;

import io.sited.AbstractModule;
import io.sited.Binder;
import io.sited.Configurable;
import io.sited.service.impl.ServiceConfigImpl;
import io.sited.service.impl.controller.HealthCheckService;
import io.sited.service.impl.controller.HealthCheckServiceImpl;
import io.sited.service.impl.exception.BadRequestExceptionMapper;
import io.sited.service.impl.exception.DefaultExceptionMapper;
import io.sited.service.impl.exception.ForbiddenExceptionMapper;
import io.sited.service.impl.exception.NotAuthorizedExceptionMapper;
import io.sited.service.impl.exception.NotFoundExceptionMapper;
import io.sited.service.impl.exception.ValidationExceptionMapper;
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
