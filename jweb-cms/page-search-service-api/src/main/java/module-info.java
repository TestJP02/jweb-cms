/**
 * @author chi
 */
module app.jweb.page.search.api {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires app.jweb.module;
    requires app.jweb.post.api;
    requires app.jweb.service;

    exports app.jweb.page.search.api;
    exports app.jweb.page.search.api.page;
    exports app.jweb.page.search.api.history;
}