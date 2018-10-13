import app.jweb.AbstractModule;

/**
 * @author chi
 */
module app.jweb.test {
    requires transitive app.jweb.module;
    requires transitive app.jweb.database;
    requires transitive app.jweb.logback;
    requires transitive org.mockito;
    requires transitive org.junit.jupiter.api;

    requires jersey.common;
    requires jersey.server;

    exports app.jweb.test;
    uses AbstractModule;
}