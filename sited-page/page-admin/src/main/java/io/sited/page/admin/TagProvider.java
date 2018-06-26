package io.sited.page.admin;

import java.util.List;

/**
 * @author chi
 */
public interface TagProvider {
    List<String> get(String title);
}
