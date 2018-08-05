/**
 * @author chi
 */
module sited.comment.admin {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires sited.admin;
    requires java.ws.rs;
    requires sited.module;
    requires sited.comment.api;
    requires sited.page.admin;
    requires sited.web;

    exports io.sited.comment.admin;
}