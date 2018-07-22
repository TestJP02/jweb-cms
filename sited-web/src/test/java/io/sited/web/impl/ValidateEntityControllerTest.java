package io.sited.web.impl;

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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({WebModule.class, ValidateEntityControllerTest.TestModule.class})
public class ValidateEntityControllerTest {
    @Inject
    MockApp app;

    @Test
    void request() {
        TestRequest request = new TestRequest();
        ContainerResponse response = app.put("/test").setEntity(request).execute();
        assertEquals(400, response.getStatus());
    }

    public static class TestModule extends AbstractWebModule {
        public TestModule() {
            super("test", Lists.newArrayList("sited.web"));
        }

        @Override
        protected void configure() {
            web().controller(TestController.class);
        }
    }

    @Path("/test")
    public static class TestController {
        @PUT
        public TestResponse request(@Valid TestRequest request) {
            TestResponse testResponse = new TestResponse();
            testResponse.response = request.request;
            return testResponse;
        }
    }

    public static class TestRequest {
        @NotNull
        public String request;
    }

    public static class TestResponse {
        public String response;
    }
}
