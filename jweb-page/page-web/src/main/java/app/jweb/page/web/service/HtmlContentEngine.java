package app.jweb.page.web.service;

import app.jweb.page.web.ContentEngine;

/**
 * @author chi
 */
public class HtmlContentEngine implements ContentEngine {
    @Override
    public String render(String content) {
        return content;
    }
}
