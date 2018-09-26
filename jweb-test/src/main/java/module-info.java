import app.jweb.AbstractModule;

/**
 * @author chi
 */
module app.jweb.test {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires app.jweb.module;
    requires app.jweb.database;
    requires app.jweb.logback;
    requires java.persistence;
    requires jersey.common;
    requires jersey.server;
    requires org.mockito;
    requires org.junit.jupiter.api;

    exports app.jweb.test;
    uses AbstractModule;
}