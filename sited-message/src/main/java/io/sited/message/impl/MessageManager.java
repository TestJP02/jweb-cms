package io.sited.message.impl;

import com.google.common.collect.Maps;
import io.sited.message.MessageHandler;
import io.sited.message.MessagePublisher;
import io.sited.message.MessageVendor;
import io.sited.message.TopicOptions;

import java.util.Map;

/**
 * @author chi
 */
public class MessageManager {
    private final Map<Class<?>, MessagePublisher<?>> topics = Maps.newConcurrentMap();
    private final MessageVendor messageVendor;

    public MessageManager(MessageVendor messageVendor) {
        this.messageVendor = messageVendor;
    }

    @SuppressWarnings("unchecked")
    public <T> MessagePublisher<T> createTopic(Class<T> messageClass, TopicOptions topicOptions) {
        return (MessagePublisher<T>) topics.computeIfAbsent(messageClass, message -> messageVendor.createTopic((Class<T>) message, topicOptions));
    }

    public <T> void listen(String messageGroup, Class<T> messageClass, MessageHandler<T> handler) {
        messageVendor.listen(messageGroup, messageClass, handler);
    }

    public void start() {
        messageVendor.start();
    }

    public void stop() {
        messageVendor.stop();
    }
}
