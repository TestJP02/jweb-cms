package io.sited.service;

import com.google.common.collect.Lists;
import io.sited.AbstractModule;
import io.sited.service.impl.exception.ExceptionResponse;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({ServiceModule.class, ServiceModuleTest.TestServiceModule.class})
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
        TestService serviceImpl2 = module.require(TestServiceImpl.class);
        ContainerResponse response = app.get("/api").execute();
        Map<String, Integer> entity = (Map<String, Integer>) response.getEntity();
        assertEquals(serviceImpl.hashCode(), serviceImpl2.hashCode());
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

    public static class TestServiceModule extends AbstractServiceModule {
        protected TestServiceModule() {
            super("service.test", Lists.newArrayList("sited.service"));
        }

        @Override
        protected void configure() {
            api().service(TestService.class, TestServiceImpl.class);
        }
    }

    @Path("/api")
    interface TestService {
        @GET
        Map<String, Integer> get();

        @Path("/exception")
        @GET
        void throwException() throws Exception;
    }

    @Singleton
    @Path("/api")
    public static class TestServiceImpl implements TestService {
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @Override
        public Map<String, Integer> get() {
            return Collections.singletonMap("hashCode", hashCode());
        }

        @GET
        @Path("/exception")
        @Produces(MediaType.APPLICATION_JSON)
        @Override
        public void throwException() throws Exception {
            throw new Exception("exception");
        }
    }
}