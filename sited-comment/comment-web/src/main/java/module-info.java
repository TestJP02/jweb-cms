/**
 * @author chi
 */
module sited.comment.web {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires sited.template;
    requires java.ws.rs;
    requires sited.web;
    requires sited.module;
    requires sited.service;
    requires sited.user.api;
    requires sited.page.web;
    requires sited.comment.api;

    exports io.sited.comment.web;
}