/**
 * @author chi
 */
module app.jweb.service {
    requires transitive app.jweb.module;

    requires jersey.client;
    requires javassist;
    requires jersey.bean.validation;
    requires jersey.media.json.jackson;

    exports app.jweb.service;
}