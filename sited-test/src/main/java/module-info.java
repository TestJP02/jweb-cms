/**
 * @author chi
 */
module sited.test {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires sited.database;
    requires sited.logback;
    requires java.persistence;
    requires jersey.common;
    requires jersey.server;
    requires org.mockito;
    requires org.junit.jupiter.api;

    exports io.sited.test;
    uses io.sited.AbstractModule;
}