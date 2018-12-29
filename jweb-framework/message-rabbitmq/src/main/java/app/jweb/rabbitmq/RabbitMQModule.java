package app.jweb.rabbitmq;

import app.jweb.message.MessageModule;
import app.jweb.message.MessageVendor;
import app.jweb.rabbitmq.impl.RabbitMQClient;
import app.jweb.rabbitmq.impl.RabbitMQOptions;
import app.jweb.rabbitmq.impl.RabbitMQVendor;


/**
 * @author chi
 */
public class RabbitMQModule extends MessageModule {
    @Override
    protected MessageVendor vendor() {
        return new RabbitMQVendor(new RabbitMQClient(options("rabbitmq", RabbitMQOptions.class)));
    }
}