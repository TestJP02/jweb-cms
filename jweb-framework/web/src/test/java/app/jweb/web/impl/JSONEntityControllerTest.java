package app.jweb.web.impl;

import com.google.common.collect.Lists;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import app.jweb.test.MockApp;
import app.jweb.web.AbstractWebModule;
import app.jweb.web.WebModule;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({WebModule.class, JSONEntityControllerTest.TestModule.class})
public class JSONEntityControllerTest {
    @Inject
    MockApp app;

    @Test
    void request() {
        TestRequest request = new TestRequest();
        request.request = "test";
        ContainerResponse response = app.put("/test").setEntity(request).execute();
        assertEquals(200, response.getStatus());
        TestResponse entity = (TestResponse) response.getEntity();
        assertEquals("test", entity.response);
    }

    public static class TestModule extends AbstractWebModule {
        public TestModule() {
            super("test", Lists.newArrayList("jweb.web"));
        }

        @Override
        protected void configure() {
            web().controller(TestController.class);
        }
    }

    @Path("/test")
    public static class TestController {
        @PUT
        public TestResponse request(TestRequest request) {
            TestResponse testResponse = new TestResponse();
            testResponse.response = request.request;
            return testResponse;
        }
    }

    public static class TestRequest {
        public String request;
    }

    public static class TestResponse {
        public String response;
    }
}
