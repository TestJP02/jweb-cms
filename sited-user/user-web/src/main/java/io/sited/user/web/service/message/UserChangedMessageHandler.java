package io.sited.user.web.service.message;

import io.sited.message.MessageHandler;
import io.sited.user.api.user.UserChangedMessage;
import io.sited.user.web.service.UserCacheService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class UserChangedMessageHandler implements MessageHandler<UserChangedMessage> {
    @Inject
    UserCacheService userCacheService;

    @Override
    public void handle(UserChangedMessage userChangedMessage) throws Throwable {
        userCacheService.invalidate(userChangedMessage.id);
    }
}
