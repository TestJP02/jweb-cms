package io.sited.file.api;


import io.sited.resource.FileResourceRepository;
import io.sited.resource.Resource;
import io.sited.service.AbstractServiceModule;

import java.nio.file.Path;
import java.util.Optional;

/**
 * @author chi
 */
public class FileModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        FileOptions options = options("file", FileOptions.class);

        api().service(FileWebService.class, options.url);
        api().service(DirectoryWebService.class, options.url);

        bind(FileRepository.class).toInstance(repository(options.dir));
    }

    protected FileRepository repository(String dir) {
        Path path = app().dir().resolve(dir);
        FileResourceRepository fileResourceRepository = new FileResourceRepository(path);
        return new FileRepository() {
            @Override
            public Optional<Resource> get(String path) {
                return fileResourceRepository.get(path);
            }

            @Override
            public void create(Resource resource) {
                fileResourceRepository.create(resource);
            }

            @Override
            public void delete(String path) {
                fileResourceRepository.delete(path);
            }
        };
    }
}
