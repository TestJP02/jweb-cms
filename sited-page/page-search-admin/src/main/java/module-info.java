/**
 * @author chi
 */
module sited.page.search.admin {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires sited.admin;
    requires sited.page.search.api;
    requires sited.page.admin;

    exports io.sited.page.search.admin;
}