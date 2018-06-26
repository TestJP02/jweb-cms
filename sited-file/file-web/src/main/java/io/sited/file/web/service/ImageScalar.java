package io.sited.file.web.service;


import io.sited.resource.Resource;

/**
 * @author chi
 */
public interface ImageScalar {
    byte[] scale(Resource image, ImageSize size);
}
