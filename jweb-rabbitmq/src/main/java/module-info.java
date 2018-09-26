/**
 * @author chi
 */
module app.jweb.rabbitmq {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires app.jweb.module;
    requires app.jweb.message;
    requires com.rabbitmq.client;
    requires commons.pool2;

    exports app.jweb.rabbitmq;
}