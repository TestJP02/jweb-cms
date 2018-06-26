package io.sited.message;

import com.google.common.collect.ImmutableList;
import io.sited.AbstractModule;
import io.sited.Binder;
import io.sited.Configurable;
import io.sited.message.impl.LocalMessageVendor;
import io.sited.message.impl.MessageConfigImpl;
import io.sited.message.impl.MessageManager;

import java.util.List;

/**
 * @author chi
 */
public class MessageModule extends AbstractModule implements Configurable<MessageConfig> {
    private MessageManager messageManager;

    @Override
    protected void configure() {
        messageManager = new MessageManager(vendor());
        bind(MessageManager.class).toInstance(messageManager);
    }

    protected MessageVendor vendor() {
        return new LocalMessageVendor();
    }

    @Override
    protected void onStartup() {
        messageManager.start();
    }

    @Override
    protected void onShutdown() {
        messageManager.stop();
    }

    @Override
    public List<String> declareRoles() {
        return ImmutableList.of();
    }

    @Override
    public MessageConfig configurator(AbstractModule module, Binder binder) {
        return new MessageConfigImpl(binder, module, messageManager);
    }
}