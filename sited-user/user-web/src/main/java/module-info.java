/**
 * @author chi
 */
module sited.user.web {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires java.transaction;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.ws.rs;
    requires sited.module;
    requires sited.web;
    requires sited.page.web;
    requires sited.template;
    requires sited.message;
    requires sited.cache;
    requires sited.user.api;
    requires sited.captcha.web;
    requires sited.pincode.api;
    requires sited.pincode.web;
    requires sited.page.api;

    exports io.sited.user.web;
}