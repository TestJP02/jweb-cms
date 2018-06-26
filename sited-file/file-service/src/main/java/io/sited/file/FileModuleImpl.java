package io.sited.file;


import io.sited.database.DatabaseConfig;
import io.sited.database.DatabaseModule;
import io.sited.file.api.DirectoryWebService;
import io.sited.file.api.FileModule;
import io.sited.file.api.FileOptions;
import io.sited.file.api.FileRepository;
import io.sited.file.api.FileWebService;
import io.sited.file.api.directory.CreateDirectoryRequest;
import io.sited.file.api.directory.DirectoryResponse;
import io.sited.file.domain.Directory;
import io.sited.file.domain.File;
import io.sited.file.service.DirectoryService;
import io.sited.file.service.FileService;
import io.sited.file.service.FileSynchronizeService;
import io.sited.file.service.ResourceManager;
import io.sited.file.web.DirectoryWebServiceImpl;
import io.sited.file.web.FileWebServiceImpl;
import io.sited.resource.FileResourceRepository;

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
    }

    @Override
    public void onStartup() {
        DirectoryWebService directoryWebService = require(DirectoryWebService.class);
        Optional<DirectoryResponse> root = directoryWebService.findByPath("/");
        if (!root.isPresent()) {
            CreateDirectoryRequest createDirectoryRequest = new CreateDirectoryRequest();
            createDirectoryRequest.path = "/";
            createDirectoryRequest.requestBy = "init";
            directoryWebService.create(createDirectoryRequest);
        }
    }
}
