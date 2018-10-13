import app.jweb.AbstractModule;
import app.jweb.web.WebModule;

/**
 * @author chi
 */
module app.jweb.web {
    requires transitive app.jweb.template;
    requires jedis;
    requires jersey.common;

    exports app.jweb.web;
    provides AbstractModule with WebModule;
}