package io.sited.service.impl;

import org.junit.jupiter.api.Test;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class ServiceDelegateBuilderTest {
    @Test
    void build() {
        ServiceDelegateBuilder<TestService> builder = new ServiceDelegateBuilder<>(TestService.class);
        Class<? extends TestService> type = builder.build();
        Path path = type.getDeclaredAnnotation(Path.class);
        assertEquals("/api/test", path.value());
    }

    @Test
    void buildWithParam() throws NoSuchMethodException {
        ServiceDelegateBuilder<TestServiceWithParameter> builder = new ServiceDelegateBuilder<>(TestServiceWithParameter.class);
        Class<? extends TestServiceWithParameter> type = builder.build();
        Method method = type.getMethod("names", String.class);
        PathParam pathParam = (PathParam) method.getParameterAnnotations()[0][0];
        assertEquals("id", pathParam.value());
    }

    @Test
    void buildWithArray() throws NoSuchMethodException {
        ServiceDelegateBuilder<TestServiceWithArrayAnnotation> builder = new ServiceDelegateBuilder<>(TestServiceWithArrayAnnotation.class);
        Class<? extends TestServiceWithArrayAnnotation> type = builder.build();
        Method method = type.getMethod("names");
        RolesAllowed rolesAllowed = method.getDeclaredAnnotation(RolesAllowed.class);
        assertEquals("role1", rolesAllowed.value()[0]);
        assertEquals("role2", rolesAllowed.value()[1]);
    }


    @Path("/api/test")
    public interface TestService {
        @GET
        @Path("/id")
        List<String> names();
    }

    @Path("/api/test")
    public interface TestServiceWithParameter {
        @GET
        @Path("/{id}")
        List<String> names(@PathParam("id") String id);
    }

    @Path("/api/test")
    public interface TestServiceWithArrayAnnotation {
        @GET
        @Path("/{id}")
        @RolesAllowed({"role1", "role2"})
        List<String> names();
    }
}