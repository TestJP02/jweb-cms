package io.sited.page.web.service;

import io.sited.page.api.comment.CommentResponse;
import io.sited.user.api.user.UserView;
import io.sited.util.collection.QueryResponse;

/**
 * @author chi
 */
public class UserCommentNodeResponse {
    public UserView user;

    public CommentResponse comment;

    public QueryResponse<UserCommentNodeResponse> children;
}
