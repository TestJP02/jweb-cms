package app.jweb.pincode.web;

import app.jweb.pincode.PinCodeModuleImpl;
import app.jweb.pincode.api.pincode.CreatePinCodeRequest;
import app.jweb.pincode.api.pincode.PinCodeResponse;
import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import app.jweb.test.MockApp;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PinCodeModuleImpl.class, DatabaseModule.class, MessageModule.class,
    ServiceModule.class})
public class PinCodeWebServiceImplTest {
    @Inject
    MockApp app;

    @Test
    public void create() {
        CreatePinCodeRequest request = new CreatePinCodeRequest();
        request.phone = "12334";
        request.requestBy = "web";
        request.ip = "127.0.0.1";
        ContainerResponse response = app.post("/api/pincode").setEntity(request).execute();
        assertEquals(200, response.getStatus());
        PinCodeResponse pinCodeResponse = (PinCodeResponse) response.getEntity();
        assertNotNull(pinCodeResponse.code);
    }

}