/**
 * @author chi
 */
module sited.user.admin {
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
    requires sited.admin;
    requires sited.web;
    requires sited.user.api;
    requires sited.captcha.web;
    exports io.sited.user.admin;
}