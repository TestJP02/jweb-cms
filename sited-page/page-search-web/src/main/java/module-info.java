/**
 * @author chi
 */
module sited.page.search.web {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires sited.page.api;
    requires sited.web;
    requires sited.template;
    requires sited.page.search.api;
    requires sited.page.web;

    exports io.sited.page.search.web;
}