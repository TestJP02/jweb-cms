/**
 * @author chi
 */
module jweb.rabbitmq {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
   requires jweb.module;
   requires jweb.message;
    requires amqp.client;
    requires commons.pool2;

    exports app.jweb.rabbitmq;
}