package app.jweb.page.web.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.page.api.page.PageChangedMessage;
import app.jweb.page.web.service.TemplateCacheService;
import app.jweb.template.TemplateEngine;

import javax.inject.Inject;

/**
 * @author chi
 */
public class TemplateChangedMessageHandler implements MessageHandler<PageChangedMessage> {
    @Inject
    TemplateEngine templateEngine;
    @Inject
    TemplateCacheService templateCacheService;

    @Override
    public void handle(PageChangedMessage pageChangedMessage) throws Throwable {
        templateEngine.reload(pageChangedMessage.templatePath);
        templateCacheService.reload(pageChangedMessage.templatePath);
    }
}
