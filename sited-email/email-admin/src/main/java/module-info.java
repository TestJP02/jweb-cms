/**
 * @author chi
 */
module sited.email.admin {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires java.transaction;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.ws.rs;
    requires sited.module;
    requires sited.service;
    requires sited.email.api;
    requires sited.admin;

    exports io.sited.email.admin;
}