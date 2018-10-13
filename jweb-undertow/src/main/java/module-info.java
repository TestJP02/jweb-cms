/**
 * @author chi
 */
module app.jweb.undertow {
    requires transitive app.jweb.module;
    requires undertow.core;
    requires xnio.nio;
    requires xnio.api;
    requires jersey.server;
    requires jersey.common;

    exports app.jweb.undertow;
}