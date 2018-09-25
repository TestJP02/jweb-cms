package app.jweb.service.impl;

import app.jweb.AbstractModule;
import com.google.common.collect.Lists;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
class ProxyResourceClientBuilderTest {
    @Test
    public void build() {
        JerseyClient client = new JerseyClientBuilder().build();
        ResourceClient resourceClient = new ResourceClient("http://localhost:8080", client);
        ProxyResourceClientBuilder<TestService> builder = new ProxyResourceClientBuilder<>(TestService.class, module(), resourceClient);
        TestService service = builder.build();
        assertNotNull(service);
    }

    private AbstractModule module() {
        return new AbstractModule("test", Lists.newArrayList()) {
            @Override
            protected void configure() {
            }
        };
    }

}