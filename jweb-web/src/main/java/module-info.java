import app.jweb.AbstractModule;
import app.jweb.web.WebModule;

/**
 * @author chi
 */
module app.jweb.web {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.validation;
    requires java.ws.rs;
    requires jedis;
    requires jersey.common;
    requires app.jweb.template;
    requires app.jweb.module;

    exports app.jweb.web;

    provides AbstractModule with WebModule;
}