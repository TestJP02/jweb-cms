/**
 * @author chi
 */
module app.jweb.undertow {
    requires com.google.common;
    requires java.xml.bind;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires app.jweb.module;
    requires undertow.core;
    requires xnio.nio;
    requires xnio.api;
    requires jersey.server;
    requires jersey.common;

    exports app.jweb.undertow;
}