/**
 * @author chi
 */
module sited.database {
    requires java.xml.bind;
    requires java.transaction;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.management;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires com.google.common;
    requires sited.module;
    requires java.sql;
    requires aopalliance.repackaged;

    requires org.hibernate.orm.core;
    requires commons.dbcp2;
    requires java.persistence;

    exports io.sited.database;
}