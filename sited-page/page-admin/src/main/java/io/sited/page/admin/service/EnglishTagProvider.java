package io.sited.page.admin.service;

import com.google.common.collect.Lists;
import io.sited.page.admin.TagProvider;

import java.util.List;

/**
 * @author chi
 */
public class EnglishTagProvider implements TagProvider {
    @Override
    public List<String> get(String title) {
        List<String> result = Lists.newArrayList();
        StringBuilder b = new StringBuilder();
        boolean space = true;
        for (char c : title.trim().toCharArray()) {
            if (Character.isWhitespace(c)) {
                if (!space) {
                    result.add(b.toString());
                    b = new StringBuilder();
                    space = true;
                }
            } else {
                space = false;
                b.append(c);
            }
        }
        if (b.length() > 0) {
            result.add(b.toString());
        }
        return result;
    }
}
