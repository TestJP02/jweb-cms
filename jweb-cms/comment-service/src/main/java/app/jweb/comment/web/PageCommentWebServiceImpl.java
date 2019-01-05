package app.jweb.comment.web;


import app.jweb.comment.api.PageCommentWebService;
import app.jweb.comment.api.comment.BatchDeleteCommentRequest;
import app.jweb.comment.api.comment.CommentNodeResponse;
import app.jweb.comment.api.comment.CommentQuery;
import app.jweb.comment.api.comment.CommentResponse;
import app.jweb.comment.api.comment.CommentTreeQuery;
import app.jweb.comment.api.comment.CreateCommentRequest;
import app.jweb.comment.api.comment.UpdateCommentRequest;
import app.jweb.comment.api.comment.VoteCommentRequest;
import app.jweb.comment.domain.PageComment;
import app.jweb.comment.domain.PageCommentVoteTracking;
import app.jweb.comment.service.PageCommentService;
import app.jweb.comment.service.PageCommentVoteTrackingService;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.List;

/**
 * @author chi
 */
public class PageCommentWebServiceImpl implements PageCommentWebService {
    @Inject
    PageCommentService pageCommentService;
    @Inject
    PageCommentVoteTrackingService pageCommentVoteTrackingService;

    @Override
    public CommentResponse get(String id) {
        return response(pageCommentService.get(id));
    }

    @Override
    public QueryResponse<CommentResponse> find(CommentQuery query) {
        return pageCommentService.find(query).map(this::response);
    }

    @Override
    public QueryResponse<CommentNodeResponse> tree(CommentTreeQuery query) {
        return pageCommentService.tree(query);
    }

    @Override
    public CommentResponse create(CreateCommentRequest request) {
        PageComment comment = pageCommentService.create(request);
        return response(comment);
    }

    @Override
    public CommentResponse toggleVote(String id, VoteCommentRequest request) {
        PageCommentVoteTracking commentVoteTracking = pageCommentVoteTrackingService.toggle(id, request);
        if (Boolean.TRUE.equals(commentVoteTracking.canceled)) {
            return response(pageCommentService.decreaseVoteDown(id, 1));
        } else {
            return response(pageCommentService.increaseVoteUp(id, 1));
        }
    }

    @Override
    public CommentResponse update(String id, UpdateCommentRequest request) {
        return response(pageCommentService.update(id, request));
    }

    @Override
    public void delete(String id, String requestBy) {
        PageComment comment = pageCommentService.get(id);
        pageCommentService.delete(id, requestBy);
    }

    @Override
    public void batchDelete(BatchDeleteCommentRequest request) {
        List<PageComment> comments = pageCommentService.batchGet(request.ids);
        request.ids.forEach(id -> pageCommentService.delete(id, request.requestBy));
    }

    private CommentResponse response(PageComment comment) {
        CommentResponse response = new CommentResponse();
        response.id = comment.id;
        response.pageId = comment.pageId;
        response.userId = comment.userId;
        response.ip = comment.ip;
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
}
