/**
 * @author chi
 */
module sited.email {
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
    requires sited.template;
    requires sited.service;
    requires sited.email.api;
    requires sited.database;
    requires sited.message;
    requires sited.web;

    exports io.sited.email;
}