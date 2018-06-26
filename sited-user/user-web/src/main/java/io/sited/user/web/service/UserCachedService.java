package io.sited.user.web.service;

import io.sited.cache.Cache;
import io.sited.user.api.UserWebService;
import io.sited.user.api.user.UserResponse;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class UserCachedService {
    @Inject
    UserWebService userWebService;

    @Inject
    Cache<UserCachedView> cache;

    public UserCachedView get(String userId) {
        Optional<UserCachedView> cached = cache.get(userId);
        if (cached.isPresent()) {
            return cached.get();
        }
        UserResponse user = userWebService.get(userId);
        UserCachedView userCachedView = new UserCachedView();
        userCachedView.username = user.username;
        userCachedView.nickname = user.nickname;
        userCachedView.imageURL = user.imageURL;
        cache.put(userId, userCachedView);
        return userCachedView;
    }

    public void invalidate(String userId) {
        cache.delete(userId);
    }
}
