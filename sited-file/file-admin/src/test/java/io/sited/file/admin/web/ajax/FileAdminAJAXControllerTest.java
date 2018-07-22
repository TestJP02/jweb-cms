package io.sited.file.admin.web.ajax;

import io.sited.admin.AdminModule;
import io.sited.database.DatabaseModule;
import io.sited.file.FileModuleImpl;
import io.sited.file.admin.FileAdminModule;
import io.sited.resource.StringResource;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.web.WebModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({FileAdminModule.class, FileModuleImpl.class, DatabaseModule.class, AdminModule.class, WebModule.class,
    ServiceModule.class})
public class FileAdminAJAXControllerTest {
    @Test
    public void hash() {
        StringResource resource = new StringResource("test.jpg", "test test test");
        String path = new FileAdminAJAXController().path("/upload/", resource);
        assertNotNull(path);
    }
}