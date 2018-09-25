package app.jweb.service.impl;

import javax.ws.rs.NotFoundException;
import java.util.UUID;

/**
 * @author chi
 */
public class TestWebServiceImpl implements TestWebService {
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
