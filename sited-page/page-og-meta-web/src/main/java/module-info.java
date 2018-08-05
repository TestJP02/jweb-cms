/**
 * @author chi
 */
module sited.page.meta {
    requires com.google.common;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires sited.web;
    requires sited.page.web;
    requires sited.template;
    exports io.sited.page.meta.web;
}