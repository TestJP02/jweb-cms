package app.jweb.service.impl;

import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author chi
 */
//@Disabled

@ExtendWith(AppExtension.class)
@Install({ServiceModule.class, TestClientModuleImpl.class})
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
}
