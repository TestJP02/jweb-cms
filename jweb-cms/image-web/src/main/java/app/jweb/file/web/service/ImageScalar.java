package app.jweb.file.web.service;


import app.jweb.resource.Resource;

import java.nio.file.Path;

/**
 * @author chi
 */
public interface ImageScalar {
    void scale(Resource image, ImageSize size, Path targetPath);
}
