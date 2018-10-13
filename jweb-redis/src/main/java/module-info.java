/**
 * @author chi
 */
module app.jweb.redis {
    requires transitive app.jweb.module;
    requires transitive app.jweb.cache;
    requires jedis;
    exports app.jweb.redis;
}