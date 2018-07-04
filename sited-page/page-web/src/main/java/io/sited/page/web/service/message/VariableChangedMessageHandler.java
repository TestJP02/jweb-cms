package io.sited.page.web.service.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.variable.VariableChangedMessage;
import io.sited.page.web.service.VariableCacheService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class VariableChangedMessageHandler implements MessageHandler<VariableChangedMessage> {
    @Inject
    VariableCacheService variableCacheService;

    @Override
    public void handle(VariableChangedMessage variableChangedMessage) throws Throwable {
        variableCacheService.reloadAll();
    }
}
