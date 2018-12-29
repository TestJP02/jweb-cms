package app.jweb.resource;

import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class ClasspathResourceRepository implements ResourceRepository {
    private final String basePath;

    public ClasspathResourceRepository(String basePath) {
        this.basePath = "".equals(basePath) ? "" : !basePath.endsWith("/") ? basePath + '/' : basePath;
    }

    @Override
    public Optional<Resource> get(String path) {
        ClasspathResource resource = new ClasspathResource(path, fullPath(path));

        try (InputStream inputStream = resource.openStream()) {
            return inputStream == null ? Optional.empty() : Optional.of(resource);
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    private String fullPath(String path) {
        return basePath + (path.length() > 0 && path.charAt(0) == '/' ? path.substring(1) : path);
    }

    @Override
    public List<Resource> list(String directory) {
        try {
            ClassPath classpath = ClassPath.from(Thread.currentThread().getContextClassLoader());
            List<Resource> resources = Lists.newArrayList();
            for (ClassPath.ResourceInfo resourceInfo : classpath.getResources()) {
                if (resourceInfo.getResourceName().startsWith(basePath)) {
                    String name = resourceInfo.getResourceName();
                    String path = name.substring(basePath.length());
                    if (path.startsWith(directory)) {
                        resources.add(new ClasspathResource(path, name));
                    }
                }
            }
            return resources;
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }
}
