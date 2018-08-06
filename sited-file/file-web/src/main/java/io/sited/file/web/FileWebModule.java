package io.sited.file.web;

import io.sited.file.web.service.FileService;
import io.sited.file.web.service.ImageScalar;
import io.sited.file.web.service.ImageScalarImpl;
import io.sited.file.web.web.FileController;
import io.sited.file.web.web.ImageController;
import io.sited.file.web.web.ajax.DirectoryAJAXController;
import io.sited.file.web.web.ajax.FileAJAXController;
import io.sited.web.AbstractWebModule;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 * @author chi
 */
public class FileWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        FileWebOptions options = options("file-web", FileWebOptions.class);
        bind(FileWebOptions.class).toInstance(options);

        if ("java-2d".equals(options.imageScalar)) {
            bind(ImageScalar.class).toInstance(new ImageScalarImpl());
        } else {
            throw new RuntimeException("Not implement yet");
        }

        if (!options.directoryEnabled) {
            return;
        }

        web().createCache("image");
        bind(FileService.class);

        web().controller(ImageController.class);
        web().controller(FileController.class);
        web().controller(FileAJAXController.class);
        web().controller(DirectoryAJAXController.class);
        app().register(MultiPartFeature.class);
//        web().addComponent(FileComponent.class);
//        templateEngine.addComponent(new IncludeComponent("uploader", "file/include/uploader.html", templateEngine));
//        templateEngine.addComponent(new IncludeComponent("directory", "file/include/directory.html", templateEngine));
        message("conf/messages/file");
    }
}
