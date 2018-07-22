/**
 * @author chi
 */
module sited.user.api {
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

    exports io.sited.user.api;
    exports io.sited.user.api.group;
    exports io.sited.user.api.token;
    exports io.sited.user.api.user;
}