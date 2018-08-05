package io.sited.comment.web;


import io.sited.comment.api.PageCommentWebService;
import io.sited.comment.api.comment.BatchDeleteCommentRequest;
import io.sited.comment.api.comment.CommentNodeResponse;
import io.sited.comment.api.comment.CommentQuery;
import io.sited.comment.api.comment.CommentResponse;
import io.sited.comment.api.comment.CommentTreeQuery;
import io.sited.comment.api.comment.CreateCommentRequest;
import io.sited.comment.api.comment.UpdateCommentRequest;
import io.sited.comment.api.comment.VoteCommentRequest;
import io.sited.comment.domain.PageComment;
import io.sited.comment.domain.PageCommentVoteTracking;
import io.sited.comment.service.PageCommentService;
import io.sited.comment.service.PageCommentVoteTrackingService;
import io.sited.message.MessagePublisher;
import io.sited.page.api.page.CommentCreatedMessage;
import io.sited.page.api.page.CommentDeletedMessage;
import io.sited.util.collection.QueryResponse;

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
    @Inject
    MessagePublisher<CommentCreatedMessage> commentCreatedMessageMessagePublisher;
    @Inject
    MessagePublisher<CommentDeletedMessage> commentDeletedMessageMessagePublisher;

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

        CommentCreatedMessage message = new CommentCreatedMessage();
        message.pageId = request.pageId;
        message.commentId = comment.id;
        commentCreatedMessageMessagePublisher.publish(message);

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

        CommentDeletedMessage message = new CommentDeletedMessage();
        message.pageId = comment.pageId;
        message.commentId = comment.id;
        commentDeletedMessageMessagePublisher.publish(message);
    }

    @Override
    public void batchDelete(BatchDeleteCommentRequest request) {
        List<PageComment> comments = pageCommentService.batchGet(request.ids);
        request.ids.forEach(id -> pageCommentService.delete(id, request.requestBy));

        for (PageComment comment : comments) {
            CommentDeletedMessage message = new CommentDeletedMessage();
            message.pageId = comment.pageId;
            message.commentId = comment.id;
            commentDeletedMessageMessagePublisher.publish(message);
        }
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
