package io.sited.file.admin.web.ajax;

import io.sited.database.DatabaseModule;
import io.sited.file.FileModuleImpl;
import io.sited.file.admin.FileAdminModule;
import io.sited.resource.StringResource;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({FileAdminModule.class, FileModuleImpl.class, DatabaseModule.class,
    ServiceModule.class})
public class FileAdminAJAXControllerTest {
    @Test
    public void hash() {
        StringResource resource = new StringResource("test.jpg", "test test test");
        String path = new FileAdminAJAXController().path("/upload/", resource);
        assertEquals("/file/upload/dfbc1a64f40bd0601e134a1cdb3cefcf.jpg", path);
    }
}