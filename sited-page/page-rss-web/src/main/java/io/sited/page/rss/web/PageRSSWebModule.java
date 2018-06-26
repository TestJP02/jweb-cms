package io.sited.page.rss.web;

import io.sited.page.rss.web.web.PageRSSController;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class PageRSSWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        bind(PageRSSOptions.class).toInstance(options("page-rss-web", PageRSSOptions.class));
        web().controller(PageRSSController.class);
    }
}
