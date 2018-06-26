/**
 * @author chi
 */
module sited.captcha.web {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires sited.web;

    exports io.sited.captcha.web;
}