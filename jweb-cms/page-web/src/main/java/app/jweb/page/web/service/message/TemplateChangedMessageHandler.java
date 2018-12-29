package app.jweb.page.web.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.page.api.template.TemplateChangedMessage;
import app.jweb.page.web.service.TemplateCacheService;
import app.jweb.template.TemplateEngine;

import javax.inject.Inject;

/**
 * @author chi
 */
public class TemplateChangedMessageHandler implements MessageHandler<TemplateChangedMessage> {
    @Inject
    TemplateEngine templateEngine;
    @Inject
    TemplateCacheService templateCacheService;

    @Override
    public void handle(TemplateChangedMessage templateChangedMessage) throws Throwable {
        templateEngine.reload(templateChangedMessage.templatePath);
        templateCacheService.reload(templateChangedMessage.templatePath);
    }
}
