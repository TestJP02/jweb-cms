/**
 * @author chi
 */
module sited.email.ses {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.ws.rs;
    requires sited.module;
    requires sited.service;
    requires sited.email;
    requires sited.email.api;
    requires sited.database;
    requires sited.message;
    requires aws.java.sdk.core;
    requires aws.java.sdk.ses;

    exports io.sited.email.ses;
}