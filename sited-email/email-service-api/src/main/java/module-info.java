/**
 * @author chi
 */
module sited.email.api {
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

    exports io.sited.email.api;
    exports io.sited.email.api.email;
    exports io.sited.email.api.template;
}