package io.sited.user.web.message;

import io.sited.message.MessageHandler;
import io.sited.user.api.user.UserChangedMessage;
import io.sited.user.web.service.UserCachedService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class UserChangedMessageHandler implements MessageHandler<UserChangedMessage> {
    @Inject
    UserCachedService userCachedService;

    @Override
    public void handle(UserChangedMessage userChangedMessage) throws Throwable {
        userCachedService.invalidate(userChangedMessage.id);
    }
}
