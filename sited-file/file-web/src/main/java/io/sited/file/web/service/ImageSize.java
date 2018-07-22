package io.sited.file.web.service;

/**
 * @author chi
 */
public class ImageSize {
    public final int width;
    public final int height;

    public ImageSize(String size) {
        if (size.charAt(0) == 'x') {
            width = 0;
            height = Integer.parseInt(size.substring(1));
        } else if (size.endsWith("x")) {
            width = Integer.parseInt(size.substring(0, size.length() - 1));
            height = 0;
        } else {
            int p = size.indexOf('x');
            width = Integer.parseInt(size.substring(0, p));
            height = Integer.parseInt(size.substring(p + 1, size.length()));
        }
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
