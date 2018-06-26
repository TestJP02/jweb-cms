package io.sited.page.web.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.template.TemplateChangedMessage;
import io.sited.template.TemplateEngine;

import javax.inject.Inject;

/**
 * @author chi
 */
public class TemplateChangedMessageHandler implements MessageHandler<TemplateChangedMessage> {
    @Inject
    TemplateEngine templateEngine;


    @Override
    public void handle(TemplateChangedMessage templateChangedMessage) throws Throwable {
        templateEngine.reload(templateChangedMessage.path);
    }
}
