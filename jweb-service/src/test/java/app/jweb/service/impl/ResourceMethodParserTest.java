package app.jweb.service.impl;

import app.jweb.AbstractModule;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class ResourceMethodParserTest {
    @Test
    void parse() {
        ResourceMethodParser resourceMethodParser = new ResourceMethodParser(TestService.class, module())
            .allowFieldType(String.class)
            .allowReturnType(List.class);

        List<ResourceMethod> methods = resourceMethodParser.parse();
        assertEquals(2, methods.size());
        assertEquals("/some/some/{id}", methods.get(0).routePath());
    }

    private AbstractModule module() {
        return new AbstractModule("test", Lists.newArrayList()) {

            @Override
            protected void configure() {
            }

            @Override
            public List<String> declareRoles() {
                return Lists.newArrayList();
            }
        };
    }

    @Path("/some")
    public interface TestService {
        @Path("/some")
        @GET
        List<String> list();

        @Path("/some/{id}")
        @GET
        List<String> get(@PathParam("id") String id);
    }
}