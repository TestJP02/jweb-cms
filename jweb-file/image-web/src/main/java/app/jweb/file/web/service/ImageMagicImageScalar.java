package app.jweb.file.web.service;


import app.jweb.ApplicationException;
import app.jweb.resource.Resource;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author chi
 */
public class ImageMagicImageScalar implements ImageScalar {
    @Override
    public void scale(Resource resource, ImageSize imageSize, Path targetPath) {
        try (InputStream inputStream = resource.openStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            int targetWidth = imageSize.width;
            int targetHeight = imageSize.height;

            if (targetHeight == 0) {
                targetHeight = imageSize.width * image.getHeight() / image.getWidth();
            }
            String size = targetWidth + "x" + targetHeight;
            String command = String.format("mogrify -filter Triangle -define filter:support=2 -thumbnail %s -background white -gravity center -extent %s -unsharp 0.25x0.25+8+0.065 -dither None " +
                "-posterize 136 -quality 80 -define jpeg:fancy-upsampling=off -define png:compression-filter=5 -define png:compression-level=9 -define png:compression-strategy=1 " +
                "-define png:exclude-chunk=all -interlace none -colorspace sRGB -strip %s", size, size, targetPath.toFile().getAbsolutePath());
            try {
                Process process = Runtime.getRuntime().exec(command);
                int resultCode = process.waitFor();
                if (resultCode != 0) {
                    readErrorOutput(process);
                }
            } catch (IOException | InterruptedException e) {
                throw new ImageException(e);
            }
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void readErrorOutput(Process process) throws IOException {
        try (InputStream stream = process.getErrorStream()) {
            byte[] bytes = ByteStreams.toByteArray(stream);
            throw new ImageException("failed to call ImageMagick, error=" + new String(bytes, Charsets.UTF_8));
        }
    }
}
