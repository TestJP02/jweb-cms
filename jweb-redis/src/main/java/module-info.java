/**
 * @author chi
 */
module jweb.redis {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
   requires jweb.module;
   requires jweb.cache;
    requires jedis;

    exports app.jweb.redis;
}