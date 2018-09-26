/**
 * @author chi
 */
module app.jweb.module {
    requires com.google.common;
    requires java.xml.bind;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;

    requires jul.to.slf4j;
    requires jackson.annotations;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires jersey.media.json.jackson;

    requires javassist;
    requires org.objectweb.asm;
    requires jersey.common;
    requires jersey.server;
    requires jersey.hk2;
    requires jersey.bean.validation;
    requires hk2.api;
    requires hk2.utils;
    requires hk2.locator;
    requires org.hibernate.validator;

    exports app.jweb;
    exports app.jweb.resource;
    exports app.jweb.util;
    exports app.jweb.util.collection;
    exports app.jweb.util.i18n;
    exports app.jweb.util.type;
    exports app.jweb.util.exception;
}