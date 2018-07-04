/**
 * @author chi
 */
module sited.page.archive.api {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.ws.rs;
    requires sited.module;
    requires sited.service;

    exports io.sited.page.archive.api;
    exports io.sited.page.archive.api.archive;
}