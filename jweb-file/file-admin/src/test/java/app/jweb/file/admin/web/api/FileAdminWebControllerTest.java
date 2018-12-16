package app.jweb.file.admin.web.api;

import app.jweb.file.admin.FileAdminModule;
import app.jweb.admin.AdminModule;
import app.jweb.database.DatabaseModule;
import app.jweb.file.FileModuleImpl;
import app.jweb.resource.StringResource;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import app.jweb.web.WebModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({FileAdminModule.class, FileModuleImpl.class, DatabaseModule.class, AdminModule.class, WebModule.class,
    ServiceModule.class})
public class FileAdminWebControllerTest {
    @Test
    public void hash() {
        StringResource resource = new StringResource("test.jpg", "test test test");
        String path = new FileAdminWebController().path("/upload/", resource);
        assertNotNull(path);
    }
}