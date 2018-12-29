package app.jweb.message;

import app.jweb.AbstractModule;
import app.jweb.Binder;
import app.jweb.Configurable;
import app.jweb.message.impl.LocalMessageVendor;
import app.jweb.message.impl.MessageConfigImpl;
import app.jweb.message.impl.MessageManager;
import com.google.common.collect.ImmutableList;

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

        onStartup(messageManager::start);
        onShutdown(messageManager::stop);
    }

    protected MessageVendor vendor() {
        return new LocalMessageVendor();
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