/**
 * @author chi
 */
module sited.comment {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires java.transaction;
    requires java.persistence;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires sited.database;
    requires java.ws.rs;
    requires sited.module;
    requires sited.service;
    requires sited.comment.api;
    requires sited.page.api;
    requires sited.message;

    exports io.sited.comment;
}