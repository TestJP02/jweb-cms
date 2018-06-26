/**
 * @author chi
 */
module sited.page.meta {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires java.transaction;
    requires javax.inject;
    requires java.ws.rs;
    requires sited.module;
    requires sited.web;
    requires sited.template;
    exports io.sited.page.meta.web;
}