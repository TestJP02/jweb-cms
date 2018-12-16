package app.jweb.file;


import app.jweb.database.DatabaseConfig;
import app.jweb.database.DatabaseModule;
import app.jweb.file.api.DirectoryWebService;
import app.jweb.file.api.FileModule;
import app.jweb.file.api.FileOptions;
import app.jweb.file.api.FileRepository;
import app.jweb.file.api.FileWebService;
import app.jweb.file.api.directory.CreateDirectoryRequest;
import app.jweb.file.api.directory.DirectoryResponse;
import app.jweb.file.domain.Directory;
import app.jweb.file.domain.File;
import app.jweb.file.service.DirectoryService;
import app.jweb.file.service.FileService;
import app.jweb.file.service.FileSynchronizeService;
import app.jweb.file.service.ResourceManager;
import app.jweb.file.web.DirectoryWebServiceImpl;
import app.jweb.file.web.FileWebServiceImpl;
import app.jweb.resource.FileResourceRepository;

import java.util.Optional;

/**
 * @author chi
 */
public class FileModuleImpl extends FileModule {
    @Override
    protected void configure() {
        FileOptions options = options("file", FileOptions.class);
        bind(FileOptions.class).toInstance(options);
        bind(FileRepository.class).toInstance(repository(options.dir));

        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig
            .entity(File.class)
            .entity(Directory.class);

        bind(DirectoryService.class);
        bind(FileService.class);
        bind(ResourceManager.class).toInstance(new ResourceManager(new FileResourceRepository(app().dir().resolve("file"))));
        bind(FileSynchronizeService.class).toInstance(requestInjection(new FileSynchronizeService(app().dir().resolve(options.dir))));
        api().service(FileWebService.class, FileWebServiceImpl.class);
        api().service(DirectoryWebService.class, DirectoryWebServiceImpl.class);

        onStartup(this::start);
    }

    private void start() {
        DirectoryWebService directoryWebService = require(DirectoryWebService.class);
        Optional<DirectoryResponse> root = directoryWebService.findByPath("/upload/");
        if (!root.isPresent()) {
            CreateDirectoryRequest createDirectoryRequest = new CreateDirectoryRequest();
            createDirectoryRequest.path = "/upload/";
            createDirectoryRequest.requestBy = "init";
            directoryWebService.create(createDirectoryRequest);
        }
    }
}
