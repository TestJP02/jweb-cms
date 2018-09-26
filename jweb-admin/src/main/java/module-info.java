/**
 * @author chi
 */
module app.jweb.admin {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires app.jweb.module;
    requires app.jweb.web;

    exports app.jweb.admin;
}