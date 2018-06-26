package io.sited.page.web.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.variable.VariableChangedMessage;
import io.sited.page.web.service.CachedVariableService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class VariableChangedMessageHandler implements MessageHandler<VariableChangedMessage> {
    @Inject
    CachedVariableService cachedVariableService;

    @Override
    public void handle(VariableChangedMessage variableChangedMessage) throws Throwable {
        cachedVariableService.reloadAll();
    }
}
