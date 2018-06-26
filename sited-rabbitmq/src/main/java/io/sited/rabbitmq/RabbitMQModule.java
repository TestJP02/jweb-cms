package io.sited.rabbitmq;

import io.sited.message.MessageModule;
import io.sited.message.MessageVendor;
import io.sited.rabbitmq.impl.RabbitMQClient;
import io.sited.rabbitmq.impl.RabbitMQOptions;
import io.sited.rabbitmq.impl.RabbitMQVendor;


/**
 * @author chi
 */
public class RabbitMQModule extends MessageModule {
    @Override
    protected MessageVendor vendor() {
        return new RabbitMQVendor(new RabbitMQClient(options("rabbitmq", RabbitMQOptions.class)));
    }
}