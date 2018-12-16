package app.jweb.comment.web.service;

import app.jweb.comment.api.comment.CommentResponse;
import app.jweb.user.api.user.UserView;
import app.jweb.util.collection.QueryResponse;

/**
 * @author chi
 */
public class UserCommentNodeResponse {
    public UserView user;

    public CommentResponse comment;

    public QueryResponse<UserCommentNodeResponse> children;
}
