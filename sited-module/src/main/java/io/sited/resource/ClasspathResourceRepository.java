package io.sited.resource;

import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
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
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void create(Resource resource) {
        throw new ResourceException("classpath resource repository is read only, path={}", basePath);
    }

    @Override
    public void delete(String path) {
        throw new ResourceException("classpath resource repository is read only, path={}", basePath);
    }

    @Override
    public Iterator<Resource> iterator() {
        try {
            ClassPath classpath = ClassPath.from(Thread.currentThread().getContextClassLoader());
            List<Resource> resources = Lists.newArrayList();
            for (ClassPath.ResourceInfo resourceInfo : classpath.getResources()) {
                if (resourceInfo.getResourceName().startsWith(basePath)) {
                    String name = resourceInfo.getResourceName();
                    resources.add(new ClasspathResource(name.substring(basePath.length()), name));
                }
            }
            return resources.iterator();
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }
}
