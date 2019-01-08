package app.jweb.page.web.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.page.api.page.PageCreatedMessage;
import app.jweb.template.TemplateEngine;

import javax.inject.Inject;

/**
 * @author chi
 */
public class TemplateChangedMessageHandler implements MessageHandler<PageCreatedMessage> {
    @Inject
    TemplateEngine templateEngine;

    @Override
    public void handle(PageCreatedMessage pageChangedMessage) throws Throwable {
        templateEngine.reload(pageChangedMessage.path);
    }
}
