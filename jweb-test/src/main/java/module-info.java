import app.jweb.AbstractModule;

/**
 * @author chi
 */
module jweb.test {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
   requires jweb.module;
   requires jweb.database;
   requires jweb.logback;
    requires java.persistence;
    requires jersey.common;
    requires jersey.server;
    requires org.mockito;
    requires org.junit.jupiter.api;

    exports app.jweb.test;
    uses AbstractModule;
}