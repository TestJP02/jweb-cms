package io.sited.page.admin.service;

import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.category.CategoryResponse;
import io.sited.user.api.UserWebService;
import io.sited.user.api.user.UserResponse;
import io.sited.web.UserInfo;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;

/**
 * @author chi
 */
public class PageCategoryAccessService {
    @Inject
    UserWebService userWebService;
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public <T> T auth(UserInfo userInfo, String categoryId, String role, UserAction<T> userAction) {
        if (!doPrivileged(userInfo, categoryId, role)) {
            throw new NotAuthorizedException("invalid action, user={}, categoryId={}, role={}", userInfo.username(), categoryId, role);
        }
        return userAction.execute();
    }

    private boolean doPrivileged(UserInfo userInfo, String categoryId, String role) {
        if (userInfo.roles().contains("*")) {
            return true;
        }
        CategoryResponse category = pageCategoryWebService.get(categoryId);
        if (category.ownerId.equals(userInfo.id())) {
            return category.ownerRoles.contains(role);
        }
        UserResponse userResponse = userWebService.get(userInfo.id());
        if (userResponse.userGroupIds.contains(category.groupId)) {
            return category.groupRoles.contains(role);
        }
        return category.othersRoles.contains(role);
    }
}
