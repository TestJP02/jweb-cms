/**
 * @author chi
 */
module sited.web {
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
    requires sited.template;
    requires sited.module;

    exports io.sited.web;

    provides io.sited.AbstractModule with io.sited.web.WebModule;
}