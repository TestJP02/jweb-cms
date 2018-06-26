package io.sited.service.impl;

import com.google.common.collect.Lists;
import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceModule;
import io.sited.service.ServiceOptions;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author chi
 */
//@Disabled

@ExtendWith(AppExtension.class)
@Install({ServiceModule.class, TestModuleTest.TestClientModule.class})
public class TestModuleTest {
    @Inject
    TestWebService testWebService;

    @Test
    void getById() {
        TestResponse response = testWebService.get("xxx");
        assertEquals("xxx", response.id);
    }

    @Test
    void notFound() {
        assertThrows(NotFoundException.class, () -> {
            testWebService.notFound();
        });
    }

    public static class TestClientModule extends AbstractServiceModule {
        protected TestClientModule() {
            super("test.api", Lists.newArrayList("sited.service"));
        }

        @Override
        protected void configure() {
            api().service(TestWebService.class, options("test", ServiceOptions.class).url);
        }
    }

    public static class TestClientModuleImpl extends AbstractServiceModule {
        protected TestClientModuleImpl() {
            super("test", Lists.newArrayList("sited.service"));
        }

        @Override
        protected void configure() {
            api().service(TestWebService.class, TestWebServiceImpl.class);
        }
    }

    @javax.ws.rs.Path("/api/test")
    public interface TestWebService {
        @GET
        TestResponse get();

        @javax.ws.rs.Path("/{id}")
        @GET
        TestResponse get(@PathParam("id") String id);

        @GET
        @javax.ws.rs.Path("/not-found/")
        TestResponse notFound();

        @POST
        TestResponse create(TestRequest request);

        @javax.ws.rs.Path("/{id}")
        @PUT
        TestResponse update(@PathParam("id") String id, TestRequest request);

        @javax.ws.rs.Path("/{id}")
        @DELETE
        void delete(@PathParam("id") String id);
    }

    public static class TestWebServiceImpl implements TestWebService {
        @Override
        public TestResponse get() {
            return new TestResponse();
        }

        @Override
        public TestResponse get(String id) {
            TestResponse response = new TestResponse();
            response.id = id;
            response.name = "test";
            return response;
        }

        @Override
        public TestResponse notFound() {
            throw new NotFoundException("missing content");
        }

        @Override
        public TestResponse create(TestRequest request) {
            TestResponse response = new TestResponse();
            response.id = UUID.randomUUID().toString();
            response.name = "test";
            return response;
        }

        @Override
        public TestResponse update(String id, TestRequest request) {
            TestResponse response = new TestResponse();
            response.id = id;
            response.name = request.name;
            return response;
        }

        @Override
        public void delete(String id) {
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestRequest {
        @XmlElement(name = "name")
        public String name;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestResponse {
        @XmlElement(name = "id")
        public String id;
        @XmlElement(name = "name")
        public String name;
    }
}
