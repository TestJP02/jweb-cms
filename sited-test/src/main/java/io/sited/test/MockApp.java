package io.sited.test;

import io.sited.App;
import io.sited.test.impl.MockContainer;
import io.sited.test.impl.RequestBuilderImpl;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.slf4j.helpers.MessageFormatter;

import java.net.URI;
import java.nio.file.Path;

/**
 * @author chi
 */
public class MockApp extends App {
    private MockContainer container;

    public MockApp(Path dir) {
        super(dir);

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(MockApp.this).to(MockApp.class);
            }
        });
    }

    public void onStartup() {
        configure();

        container = new MockContainer(this);
        container.getApplicationHandler().onStartup(container);
    }

    void onShutdown() {
        if (container != null) {
            container.getApplicationHandler().onShutdown(container);
        }
    }

    public RequestBuilder get(String path, String... args) {
        return request("GET", path, args);
    }

    public RequestBuilder post(String path, String... args) {
        return request("POST", path, args);
    }

    public RequestBuilder put(String path, String... args) {
        return request("PUT", path, args);
    }

    public RequestBuilder delete(String path, String... args) {
        return request("DELETE", path, args);
    }

    private RequestBuilder request(String httpMethod, String path, String... args) {
        return new RequestBuilderImpl(httpMethod, URI.create(MessageFormatter.arrayFormat(path, args).getMessage()), container);
    }
}
