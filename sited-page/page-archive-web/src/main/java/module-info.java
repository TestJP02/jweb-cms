/**
 * @author chi
 */
module sited.page.archive.web {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.ws.rs;
    requires sited.module;
    requires sited.web;
    requires sited.template;
    requires sited.page.web;
    requires sited.page.api;
    requires sited.service;
    requires sited.page.archive.api;

    exports io.sited.page.archive.web;
}