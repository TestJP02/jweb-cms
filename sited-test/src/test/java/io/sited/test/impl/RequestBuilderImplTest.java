package io.sited.test.impl;

import com.google.common.collect.Lists;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.web.AbstractWebModule;
import io.sited.web.WebModule;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({RequestBuilderImplTest.TestModule.class, WebModule.class})
class RequestBuilderImplTest {
    @Inject
    MockApp app;

    @Test
    void get() {
        ContainerResponse response = app.get("/test").execute();
        assertEquals(200, response.getStatus());
    }

    public static class TestModule extends AbstractWebModule {
        protected TestModule() {
            super("test", Lists.newArrayList("sited.web"));
        }

        @Override
        protected void configure() {
            web().controller(TestController.class);
        }
    }

    @Path("/test")
    public static class TestController {
        @GET
        public Response get() {
            return Response.ok().build();
        }
    }
}