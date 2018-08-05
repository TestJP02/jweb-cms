package io.sited.comment.web.web.api;


import io.sited.comment.api.PageCommentWebService;
import io.sited.comment.api.comment.CommentResponse;
import io.sited.comment.api.comment.CommentTreeQuery;
import io.sited.comment.api.comment.CreateCommentRequest;
import io.sited.comment.api.comment.VoteCommentRequest;
import io.sited.comment.web.CommentWebOptions;
import io.sited.comment.web.service.UserCommentNodeResponse;
import io.sited.comment.web.service.UserCommentNodeService;
import io.sited.comment.web.web.api.comment.CommentTreeAJAXQuery;
import io.sited.comment.web.web.api.comment.CreateCommentAJAXRequest;
import io.sited.user.api.UserWebService;
import io.sited.user.api.user.UserResponse;
import io.sited.user.api.user.UserView;
import io.sited.util.collection.QueryResponse;
import io.sited.web.ClientInfo;
import io.sited.web.UserInfo;

import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


/**
 * @author chi
 */
@Path("/web/api/comment")
public class CommentWebController {
    @Inject
    PageCommentWebService commentWebService;
    @Inject
    UserCommentNodeService userCommentNodeService;
    @Inject
    UserWebService userWebService;
    @Inject
    UserInfo userInfo;
    @Inject
    ClientInfo clientInfo;
    @Inject
    CommentWebOptions pageWebOptions;

    @Path("/page/{pageId}")
    @PUT
    public QueryResponse<UserCommentNodeResponse> find(@PathParam("pageId") String pageId, CommentTreeAJAXQuery query) {
        CommentTreeQuery commentTreeQuery = new CommentTreeQuery();
        commentTreeQuery.pageId = pageId;
        commentTreeQuery.page = query.page;
        commentTreeQuery.limit = query.limit;
        return userCommentNodeService.map(commentWebService.tree(commentTreeQuery));
    }

    @Path("/{id}/vote")
    @POST
    public CommentResponse vote(@PathParam("id") String id) {
        VoteCommentRequest voteCommentRequest = new VoteCommentRequest();
        voteCommentRequest.ip = clientInfo.ip();
        voteCommentRequest.userId = userInfo.id();
        voteCommentRequest.requestBy = "comment-web";
        return commentWebService.toggleVote(id, voteCommentRequest);
    }

    @POST
    public UserCommentNodeResponse create(CreateCommentAJAXRequest ajaxRequest) {
        if (!pageWebOptions.visitorCommentEnabled && !userInfo.isAuthenticated()) {
            throw new ForbiddenException("Please comment after login");
        }
        CreateCommentRequest createCommentRequest = new CreateCommentRequest();

        createCommentRequest.pageId = ajaxRequest.pageId;
        createCommentRequest.userId = userInfo.id();
        createCommentRequest.parentId = ajaxRequest.parentId;
        createCommentRequest.content = ajaxRequest.content;
        createCommentRequest.ip = clientInfo.ip();
        createCommentRequest.requestBy = "comment-web";
        return response(commentWebService.create(createCommentRequest));
    }

    private UserCommentNodeResponse response(CommentResponse response) {
        UserCommentNodeResponse commentAJAXResponse = new UserCommentNodeResponse();
        commentAJAXResponse.comment = response;
        if (response.userId != null) {
            UserResponse userResponse = userWebService.get(response.userId);
            UserView user = new UserView();
            user.id = userResponse.id;
            user.username = userResponse.username;
            user.nickname = userResponse.nickname;
            user.imageURL = userResponse.imageURL;
            user.createdTime = userResponse.createdTime;
            commentAJAXResponse.user = user;
        } else {
            UserView user = new UserView();
            user.nickname = response.ip;
            commentAJAXResponse.user = user;
        }
        return commentAJAXResponse;
    }
}
