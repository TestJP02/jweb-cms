package io.sited.page.admin;

/**
 * @author chi
 */
public interface PageAdminConfig {
    PageAdminConfig setPathProvider(String language, PathProvider provider);

    PageAdminConfig setTagProvider(String language, TagProvider provider);
}
