package io.sited.page.admin.service;

import io.sited.page.admin.PathProvider;

/**
 * @author chi
 */
public class EnglishPathProvider implements PathProvider {
    @Override
    public String get(String title) {
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
