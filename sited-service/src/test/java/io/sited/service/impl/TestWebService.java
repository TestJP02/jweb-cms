package io.sited.service.impl;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

/**
 * @author chi
 */
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
