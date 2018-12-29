package app.jweb.comment.web.service;

import app.jweb.comment.api.comment.CommentNodeResponse;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import app.jweb.user.api.UserWebService;
import app.jweb.user.api.user.BatchGetUserRequest;
import app.jweb.user.api.user.UserResponse;
import app.jweb.user.api.user.UserView;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class UserCommentNodeService {
    @Inject
    UserWebService userWebService;

    public QueryResponse<UserCommentNodeResponse> map(QueryResponse<CommentNodeResponse> comments) {
        List<String> userIds = userIds(comments);

        if (userIds.isEmpty()) {
            return view(comments, ImmutableMap.of());
        } else {
            BatchGetUserRequest request = new BatchGetUserRequest();
            request.ids = userIds;
            Map<String, UserView> users = userWebService.batchGet(request).stream().collect(Collectors.toMap(user -> user.id, user -> user));
            return view(comments, users);
        }
    }

    public UserResponse user(String userId) {
        return userWebService.get(userId);
    }

    private List<String> userIds(QueryResponse<CommentNodeResponse> comments) {
        List<String> userIds = Lists.newArrayList();
        for (CommentNodeResponse comment : comments) {
            String userId = comment.comment.userId;
            if (!Strings.isNullOrEmpty(userId)) {
                userIds.add(userId);
            }
            if (comment.children != null) {
                userIds.addAll(userIds(comment.children));
            }
        }
        return userIds;
    }

    private QueryResponse<UserCommentNodeResponse> view(QueryResponse<CommentNodeResponse> comments, Map<String, UserView> users) {
        QueryResponse<UserCommentNodeResponse> views = new QueryResponse<>();
        views.limit = comments.limit;
        views.page = comments.page;
        views.total = comments.total;
        views.items = Lists.newArrayList();
        for (CommentNodeResponse comment : comments) {
            UserCommentNodeResponse userCommentNodeResponse = new UserCommentNodeResponse();
            userCommentNodeResponse.comment = comment.comment;
            UserView user = users.get(comment.comment.userId);
            if (user == null) {
                user = new UserView();
                user.nickname = comment.comment.ip;
            }
            userCommentNodeResponse.user = user;
            if (comment.children != null) {
                userCommentNodeResponse.children = view(comment.children, users);
            }
            views.items.add(userCommentNodeResponse);
        }
        return views;
    }

}
