/**
 * @author chi
 */
module app.jweb.page.search.web {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires app.jweb.module;
    requires app.jweb.page.api;
    requires app.jweb.web;
    requires app.jweb.template;
    requires app.jweb.page.search.api;
    requires app.jweb.page.web;

    exports app.jweb.page.search.web;
}