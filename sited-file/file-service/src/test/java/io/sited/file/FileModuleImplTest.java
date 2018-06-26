package io.sited.file;

import io.sited.database.DatabaseModule;
import io.sited.file.api.FileWebService;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({FileModuleImpl.class, DatabaseModule.class, ServiceModule.class})
public class FileModuleImplTest {
    @Inject
    FileWebService fileWebService;

    @Test
    public void configure() {
        assertNotNull(fileWebService);
    }
}