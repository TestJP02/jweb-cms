package app.jweb.file.web.service;

import app.jweb.ApplicationException;
import app.jweb.resource.Resource;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author miller
 */
public class ImageMagicImageScalar implements ImageScalar {
    @Override
    public void scale(Resource resource, ImageSize size, Path targetPath) {
        try (InputStream inputStream = resource.openStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            int targetHeight = size.height;
            int targetWidth = size.width;
            if (targetHeight == 0) {
                targetHeight = size.width * image.getHeight() / image.getWidth();
            }
            if (targetWidth == 0) {
                targetWidth = size.height * image.getWidth() / image.getHeight();
            }

            Files.copy(inputStream, targetPath);
            transformImage(targetPath, String.format("%dx%d", targetWidth, targetHeight));
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void transformImage(Path tempImagePath, String size) {
        String command = String.format("mogrify -filter Triangle -define filter:support=2 -thumbnail %s -background white -gravity center -extent %s -unsharp 0.25x0.25+8+0.065 -dither None -posterize 136 -quality 80 -define jpeg:fancy-upsampling=off -define png:compression-filter=5 -define png:compression-level=9 -define png:compression-strategy=1 -define png:exclude-chunk=all -interlace none -colorspace sRGB -strip %s", size, size, tempImagePath);
        try {
            Process process = Runtime.getRuntime().exec(command);
            int resultCode = process.waitFor();
            if (resultCode != 0) {
                readErrorOutput(process);
            }
        } catch (IOException | InterruptedException e) {
            throw new ApplicationException(e);
        }
    }

    private void readErrorOutput(Process process) throws IOException {
        try (InputStream stream = process.getErrorStream()) {
            byte[] bytes = bytes(stream);
            throw new ApplicationException("failed to call ImageMagick, error=" + new String(bytes, Charsets.UTF_8));
        }
    }

    private byte[] bytes(InputStream stream) {
        List<byte[]> buffers = Lists.newArrayList();
        int total = 0;
        byte[] currentBuffer = new byte[1024];
        buffers.add(currentBuffer);
        int currentBufferPosition = 0;
        try {
            while (true) {
                int bytesToRead = currentBuffer.length - currentBufferPosition;
                int bytesRead = stream.read(currentBuffer, currentBufferPosition, bytesToRead);
                if (bytesRead < 0) break;
                currentBufferPosition += bytesRead;
                total += bytesRead;
                if (currentBufferPosition >= currentBuffer.length) {
                    currentBuffer = new byte[currentBuffer.length << 1];
                    buffers.add(currentBuffer);
                    currentBufferPosition = 0;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        byte[] result = new byte[total];
        int position = 0;
        for (byte[] buffer : buffers) {
            int length = Math.min(buffer.length, total - position);
            System.arraycopy(buffer, 0, result, position, length);
            position += length;
            if (position >= total) break;
        }
        return result;
    }
}
