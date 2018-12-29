package app.jweb.post.util;

/**
 * @author chi
 */
public interface URLs {
    static String normalize(String path) {
        StringBuilder b = new StringBuilder();
        boolean hyphen = false;
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '-') {
                if (!hyphen) {
                    b.append(Character.toLowerCase(c));
                    hyphen = true;
                }
            } else {
                hyphen = false;
                b.append(Character.toLowerCase(c));
            }
        }
        return b.toString();
    }

    static String segment(String name) {
        StringBuilder b = new StringBuilder();
        boolean skip = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                if (skip) {
                    b.append('-');
                }
                b.append(Character.toLowerCase(c));
                skip = false;
            } else {
                skip = true;
            }
        }
        return b.toString();
    }
}
