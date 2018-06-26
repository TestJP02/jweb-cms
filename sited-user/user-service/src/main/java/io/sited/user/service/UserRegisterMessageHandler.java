package io.sited.user.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.email.api.EmailWebService;
import io.sited.email.api.email.SendTemplateEmailRequest;
import io.sited.message.MessageHandler;
import io.sited.user.api.user.UserRegisterMessage;

import javax.inject.Inject;

/**
 * @author chi
 */
public class UserRegisterMessageHandler implements MessageHandler<UserRegisterMessage> {
    @Inject
    EmailWebService emailWebService;

    @Override
    public void handle(UserRegisterMessage userRegisterMessage) throws Throwable {
        SendTemplateEmailRequest request = new SendTemplateEmailRequest();
        request.to = Lists.newArrayList(userRegisterMessage.email);
        request.bindings = Maps.newHashMap();
        request.bindings.put("username", userRegisterMessage.username);
        request.bindings.put("email", userRegisterMessage.email);
        emailWebService.send("user-welcome", request);
    }
}
