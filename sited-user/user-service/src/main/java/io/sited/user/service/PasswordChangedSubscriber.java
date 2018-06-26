package io.sited.user.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.email.api.EmailWebService;
import io.sited.email.api.email.SendTemplateEmailRequest;
import io.sited.message.MessageHandler;
import io.sited.user.EmailTemplateOptions;
import io.sited.user.UserOptions;
import io.sited.user.api.user.UserPasswordChangedMessage;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PasswordChangedSubscriber implements MessageHandler<UserPasswordChangedMessage> {
    @Inject
    EmailWebService emailWebService;
    @Inject
    UserOptions userOptions;

    @Override
    public void handle(UserPasswordChangedMessage userPasswordChangedMessage) throws Throwable {
        EmailTemplateOptions emailOption = userOptions.passwordChangedEmail;
        SendTemplateEmailRequest sendTemplateEmailRequest = new SendTemplateEmailRequest();
        sendTemplateEmailRequest.from = "notification@sited.io";
        sendTemplateEmailRequest.to = Lists.newArrayList(userPasswordChangedMessage.email);
        sendTemplateEmailRequest.replyTo = userPasswordChangedMessage.email;
        sendTemplateEmailRequest.requestBy = userPasswordChangedMessage.id;
        sendTemplateEmailRequest.bindings = Maps.newHashMap();
        sendTemplateEmailRequest.bindings.put("email", userPasswordChangedMessage.email);
        sendTemplateEmailRequest.bindings.put("nickname", userPasswordChangedMessage.nickname);
        emailWebService.send(emailOption.name, sendTemplateEmailRequest);
    }
}
