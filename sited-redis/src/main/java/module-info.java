/**
 * @author chi
 */
module sited.redis {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires sited.cache;
    requires jedis;

    exports io.sited.redis;
}