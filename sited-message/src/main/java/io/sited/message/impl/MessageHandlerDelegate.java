package io.sited.message.impl;

import io.sited.AbstractModule;
import io.sited.message.MessageHandler;

/**
 * @author chi
 */
public class MessageHandlerDelegate<T> implements MessageHandler<T> {
    private final Class<? extends MessageHandler<T>> handlerClass;
    private final AbstractModule module;

    public MessageHandlerDelegate(Class<? extends MessageHandler<T>> handlerClass, AbstractModule module) {
        this.handlerClass = handlerClass;
        this.module = module;
    }

    @Override
    public void handle(T message) throws Throwable {
        module.require(handlerClass).handle(message);
    }
}
