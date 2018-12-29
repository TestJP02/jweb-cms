package app.jweb.user.web.service;

import app.jweb.user.api.UserWebService;
import app.jweb.user.api.user.UserResponse;
import app.jweb.cache.Cache;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class UserCacheService {
    @Inject
    UserWebService userWebService;

    @Inject
    Cache<UserCacheView> cache;

    public UserCacheView get(String userId) {
        Optional<UserCacheView> cached = cache.get(userId);
        if (cached.isPresent()) {
            return cached.get();
        }
        UserResponse user = userWebService.get(userId);
        UserCacheView userCacheView = new UserCacheView();
        userCacheView.username = user.username;
        userCacheView.nickname = user.nickname;
        userCacheView.imageURL = user.imageURL;
        userCacheView.description = user.description;
        cache.put(userId, userCacheView);
        return userCacheView;
    }

    public void invalidate(String userId) {
        cache.delete(userId);
    }
}
