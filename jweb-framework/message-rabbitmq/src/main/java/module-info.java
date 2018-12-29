/**
 * @author chi
 */
module app.jweb.rabbitmq {
    requires transitive app.jweb.module;
    requires transitive app.jweb.message;

    requires com.rabbitmq.client;
    requires commons.pool2;

    exports app.jweb.rabbitmq;
}