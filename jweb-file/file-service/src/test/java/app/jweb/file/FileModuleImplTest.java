package app.jweb.file;

import app.jweb.database.DatabaseModule;
import app.jweb.file.api.FileWebService;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
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