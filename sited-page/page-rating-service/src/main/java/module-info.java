/**
 * @author chi
 */
module sited.page.rating {
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
    requires sited.database;
    requires sited.service;
    requires sited.page.rating.api;

    exports io.sited.page.rating;
}