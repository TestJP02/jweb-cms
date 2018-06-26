/**
 * @author chi
 */
module sited.netty {
    requires com.google.common;
    requires java.xml.bind;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires undertow.core;
    requires xnio.nio;
    requires xnio.api;
    requires jersey.server;
    requires jersey.common;

    exports io.sited.undertow;
}