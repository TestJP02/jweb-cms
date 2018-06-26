/**
 * @author chi
 */
module sited.service {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires jersey.client;
    requires javassist;

    requires jersey.media.json.jackson;
    requires jersey.bean.validation;

    exports io.sited.service;
}