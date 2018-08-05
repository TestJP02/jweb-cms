/**
 * @author chi
 */
module sited.user {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires java.persistence;
    requires java.transaction;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.ws.rs;
    requires sited.module;
    requires sited.service;
    requires sited.database;
    requires sited.message;
    requires sited.user.api;

    exports io.sited.user;
}