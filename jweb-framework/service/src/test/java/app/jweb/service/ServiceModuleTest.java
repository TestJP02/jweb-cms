package app.jweb.service;

import app.jweb.AbstractModule;
import app.jweb.service.impl.exception.ExceptionResponse;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import app.jweb.test.MockApp;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({ServiceModule.class, TestServiceModule.class})
class ServiceModuleTest {
    @Inject
    MockApp app;

    @Test
    void configure() {
        assertNotNull(app);
    }

    @Test
    @SuppressWarnings("unchecked")
    void serviceSingleton() {
        AbstractModule module = app.module("service.test");
        TestService serviceImpl = module.require(TestService.class);
        ContainerResponse response = app.get("/api").execute();
        Map<String, Integer> entity = (Map<String, Integer>) response.getEntity();
        assertEquals(serviceImpl.hashCode(), (int) entity.get("hashCode"));
    }

    @Test
    void notFound() {
        ContainerResponse response = app.get("/api/not-found").execute();
        ExceptionResponse entity = (ExceptionResponse) response.getEntity();
        assertNotNull(entity.errorMessage);
    }

    @Test
    void defaultExceptionHandler() {
        ContainerResponse response = app.get("/api/exception").execute();
        ExceptionResponse entity = (ExceptionResponse) response.getEntity();
        assertNotNull(entity.errorMessage);
    }
}