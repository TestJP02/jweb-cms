package app.jweb.page.web.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.page.api.variable.VariableChangedMessage;
import app.jweb.page.web.service.VariableCacheService;

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
