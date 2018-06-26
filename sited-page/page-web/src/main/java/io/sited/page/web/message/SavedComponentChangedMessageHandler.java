package io.sited.page.web.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.component.SavedComponentChangedMessage;
import io.sited.page.api.component.SavedComponentResponse;
import io.sited.page.web.component.SavedComponent;
import io.sited.template.TemplateEngine;

import javax.inject.Inject;

/**
 * @author chi
 */
public class SavedComponentChangedMessageHandler implements MessageHandler<SavedComponentChangedMessage> {
    @Inject
    TemplateEngine templateEngine;

    @Override
    public void handle(SavedComponentChangedMessage pageTemplateChangedMessage) throws Throwable {
        templateEngine.addComponent(new SavedComponent(response(pageTemplateChangedMessage), templateEngine));
    }

    private SavedComponentResponse response(SavedComponentChangedMessage pageTemplateChangedMessage) {
        SavedComponentResponse response = new SavedComponentResponse();
        response.id = pageTemplateChangedMessage.id;
        response.name = pageTemplateChangedMessage.name;
        response.componentName = pageTemplateChangedMessage.componentName;
        response.attributes = pageTemplateChangedMessage.attributes;
        response.displayName = pageTemplateChangedMessage.displayName;
        response.updatedTime = pageTemplateChangedMessage.updatedTime;
        response.updatedBy = pageTemplateChangedMessage.updatedBy;
        response.createdTime = pageTemplateChangedMessage.createdTime;
        response.createdBy = pageTemplateChangedMessage.createdBy;
        return response;
    }
}
