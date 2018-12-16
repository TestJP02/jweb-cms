package app.jweb.file.web;

import app.jweb.file.web.service.FileService;
import app.jweb.file.web.service.ImageScalar;
import app.jweb.file.web.service.ImageScalarImpl;
import app.jweb.file.web.web.FileController;
import app.jweb.file.web.web.ImageController;
import app.jweb.file.web.web.api.DirectoryWebController;
import app.jweb.file.web.web.api.FileWebController;
import app.jweb.web.AbstractWebModule;
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
        web().controller(FileWebController.class);
        web().controller(DirectoryWebController.class);
        app().register(MultiPartFeature.class);
//        web().addComponent(FileComponent.class);
//        templateEngine.addComponent(new IncludeComponent("uploader", "file/include/uploader.html", templateEngine));
//        templateEngine.addComponent(new IncludeComponent("directory", "file/include/directory.html", templateEngine));
        message("conf/messages/file");
    }
}
