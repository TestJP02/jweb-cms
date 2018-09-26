/**
 * @author chi
 */
module app.jweb.database {
    requires java.xml.bind;
    requires java.transaction;
    requires java.management;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires com.google.common;
    requires app.jweb.module;
    requires java.sql;
    requires aopalliance.repackaged;

    requires org.hibernate.orm.core;
    requires commons.dbcp2;
    requires java.persistence;

    exports app.jweb.database;
}