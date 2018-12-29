package app.jweb.comment.admin.web;

import com.google.common.collect.Lists;
import app.jweb.comment.admin.web.comment.BatchDeleteAJAXRequest;
import app.jweb.comment.admin.web.comment.CommentAJAXNodeResponse;
import app.jweb.comment.admin.web.comment.CommentAJAXResponse;
import app.jweb.comment.admin.web.comment.CommentFindAJAXRequest;
import app.jweb.comment.admin.web.comment.CommentTreeAJAXQuery;
import app.jweb.comment.admin.web.comment.CreateCommentAJAXRequest;
import app.jweb.comment.admin.web.comment.UpdateCommentStatusRequest;
import app.jweb.comment.api.PageCommentWebService;
import app.jweb.comment.api.comment.BatchDeleteCommentRequest;
import app.jweb.comment.api.comment.CommentNodeResponse;
import app.jweb.comment.api.comment.CommentQuery;
import app.jweb.comment.api.comment.CommentResponse;
import app.jweb.comment.api.comment.CommentTreeQuery;
import app.jweb.comment.api.comment.CreateCommentRequest;
import app.jweb.comment.api.comment.UpdateCommentRequest;
import app.jweb.util.collection.QueryResponse;
import app.jweb.web.ClientInfo;
import app.jweb.web.UserInfo;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * @author chi
 */
@Path("/admin/api/comment")
public class PageCommentAdminController {
    @Inject
    PageCommentWebService commentWebService;
    @Inject
    UserInfo userInfo;
    @Inject
    ClientInfo clientInfo;

    @RolesAllowed("LIST")
    @Path("/list/page/{pageId}")
    @PUT
    public QueryResponse<CommentAJAXResponse> find(@PathParam("pageId") String pageId, CommentFindAJAXRequest request) {
        return commentWebService.find(query(pageId, request)).map(this::response);
    }

    @RolesAllowed("LIST")
    @Path("/tree/page/{pageId}")
    @PUT
    public List<CommentAJAXNodeResponse> tree(@PathParam("pageId") String pageId, CommentTreeAJAXQuery ajaxQuery) {
        CommentTreeQuery commentTreeQuery = new CommentTreeQuery();
        commentTreeQuery.pageId = pageId;
        commentTreeQuery.page = ajaxQuery.page;
        commentTreeQuery.limit = ajaxQuery.limit;
        return buildTree(commentWebService.tree(commentTreeQuery));
    }

    @RolesAllowed("CREATE")
    @POST
    public CommentAJAXResponse create(CreateCommentAJAXRequest ajaxRequest) {
        return response(commentWebService.create(request(ajaxRequest)));
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}/status")
    @PUT
    public CommentAJAXResponse updateStatus(@PathParam("id") String id, UpdateCommentStatusRequest updateCommentStatusRequest) {
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        updateCommentRequest.status = updateCommentStatusRequest.status;
        updateCommentRequest.requestBy = userInfo.id();
        return response(commentWebService.update(id, updateCommentRequest));
    }

    @RolesAllowed("DELETE")
    @Path("/batch-delete")
    @PUT
    public void batchDelete(BatchDeleteAJAXRequest batchDeleteAJAXRequest) {
        BatchDeleteCommentRequest batchDeleteCommentRequest = new BatchDeleteCommentRequest();
        batchDeleteCommentRequest.ids = batchDeleteAJAXRequest.ids;
        batchDeleteCommentRequest.requestBy = userInfo.id();
        commentWebService.batchDelete(batchDeleteCommentRequest);
    }

    private CommentAJAXResponse response(CommentResponse comment) {
        CommentAJAXResponse response = new CommentAJAXResponse();
        response.id = comment.id;
        response.pageId = comment.pageId;
        response.userId = comment.userId;
        response.status = comment.status;
        response.parentId = comment.parentId;
        response.firstParentId = comment.firstParentId;
        response.content = comment.content;
        response.totalVoteDown = comment.totalVoteDown;
        response.totalVoteUp = comment.totalVoteUp;
        response.totalReplies = comment.totalReplies;
        response.createdTime = comment.createdTime;
        response.createdBy = comment.createdBy;
        response.updatedTime = comment.updatedTime;
        response.updatedBy = comment.updatedBy;
        return response;
    }

    private CommentQuery query(String pageId, CommentFindAJAXRequest request) {
        CommentQuery instance = new CommentQuery();
        instance.pageId = pageId;
        instance.title = request.title;
        instance.status = request.status;
        instance.page = request.page;
        instance.limit = request.limit;
        return instance;
    }

    private CreateCommentRequest request(CreateCommentAJAXRequest ajaxRequest) {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.pageId = ajaxRequest.pageId;
        createCommentRequest.userId = userInfo.id();
        createCommentRequest.ip = clientInfo.ip();
        createCommentRequest.parentId = ajaxRequest.parentId;
        createCommentRequest.content = ajaxRequest.content;
        return createCommentRequest;
    }

    private List<CommentAJAXNodeResponse> buildTree(QueryResponse<CommentNodeResponse> queryResponse) {
        List<CommentAJAXNodeResponse> list = Lists.newArrayList();
        for (CommentNodeResponse node : queryResponse.items) {
            CommentAJAXNodeResponse ajaxNode = transfer(node.comment);
            ajaxNode.children = buildTree(node.children);
            list.add(ajaxNode);
        }
        return list;
    }

    private CommentAJAXNodeResponse transfer(CommentResponse comment) {
        CommentAJAXNodeResponse ajaxNode = new CommentAJAXNodeResponse();
        ajaxNode.id = comment.id;
        ajaxNode.pageId = comment.pageId;
        ajaxNode.userId = comment.userId;
        ajaxNode.ip = comment.ip;
        ajaxNode.status = comment.status;
        ajaxNode.parentId = comment.parentId;
        ajaxNode.firstParentId = comment.firstParentId;
        ajaxNode.content = comment.content;
        ajaxNode.totalVoteUp = comment.totalVoteUp;
        ajaxNode.totalVoteDown = comment.totalVoteDown;
        ajaxNode.totalReplies = comment.totalReplies;
        ajaxNode.createdTime = comment.createdTime;
        ajaxNode.updatedTime = comment.updatedTime;
        ajaxNode.createdBy = comment.createdBy;
        ajaxNode.updatedBy = comment.updatedBy;
        return ajaxNode;
    }
}