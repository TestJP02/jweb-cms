package io.sited.page.admin.service;

/**
 * @author chi
 */
public class PagePathService {
    public String suggest(String title) {
        StringBuilder b = new StringBuilder();
        boolean space = true;
        for (char c : title.trim().toCharArray()) {
            if (Character.isWhitespace(c)) {
                if (!space) {
                    b.append('-');
                    space = true;
                }
            } else {
                space = false;
                b.append(c);
            }
        }
        return b.toString();
    }
}
