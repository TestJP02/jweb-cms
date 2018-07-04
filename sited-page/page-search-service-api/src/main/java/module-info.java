/**
 * @author chi
 */
module sited.page.search.api {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires sited.page.api;
    requires sited.service;

    exports io.sited.page.search.api;
    exports io.sited.page.search.api.page;
}