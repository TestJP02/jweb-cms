/**
 * @author chi
 */
module sited.captcha.web.simple {
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
    requires sited.template;
    requires sited.captcha.web;
    requires simplecaptcha;
    requires java.desktop;

    exports io.sited.captcha.simple;
}