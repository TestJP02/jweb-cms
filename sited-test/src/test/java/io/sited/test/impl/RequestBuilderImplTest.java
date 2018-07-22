package io.sited.test.impl;

import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.web.WebModule;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({TestWebModule.class, WebModule.class})
class RequestBuilderImplTest {
    @Inject
    MockApp app;

    @Test
    void get() {
        ContainerResponse response = app.get("/test").execute();
        assertEquals(200, response.getStatus());
    }
}