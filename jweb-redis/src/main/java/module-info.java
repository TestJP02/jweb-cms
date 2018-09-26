/**
 * @author chi
 */
module app.jweb.redis {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires app.jweb.module;
    requires app.jweb.cache;
    requires jedis;

    exports app.jweb.redis;
}