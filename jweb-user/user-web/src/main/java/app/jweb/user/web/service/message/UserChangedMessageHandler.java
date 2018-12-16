package app.jweb.user.web.service.message;

import app.jweb.user.api.user.UserChangedMessage;
import app.jweb.user.web.service.UserCacheService;
import app.jweb.message.MessageHandler;

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
