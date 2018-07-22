/**
 * @author chi
 */
module sited.module {
    requires com.google.common;
    requires java.xml.bind;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires jul.to.slf4j;
    requires java.validation;
    requires java.ws.rs;

    requires jackson.annotations;
    requires jackson.dataformat.yaml;
    requires jackson.core;
    requires jackson.databind;
    requires jackson.datatype.jsr310;
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

    exports io.sited;
    exports io.sited.resource;
    exports io.sited.util;
    exports io.sited.util.collection;
    exports io.sited.util.i18n;
    exports io.sited.util.type;
    exports io.sited.util.exception;
}