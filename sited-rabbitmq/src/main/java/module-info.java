/**
 * @author chi
 */
module sited.rabbitmq {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires sited.message;
    requires amqp.client;
    requires commons.pool2;

    exports io.sited.rabbitmq;
}