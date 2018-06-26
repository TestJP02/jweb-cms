package io.sited.page.service;

import io.sited.database.Repository;
import io.sited.page.api.comment.VoteCommentRequest;
import io.sited.page.domain.PageCommentVoteTracking;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PageCommentVoteTrackingService {
    @Inject
    Repository<PageCommentVoteTracking> repository;

    @Transactional
    public PageCommentVoteTracking toggle(String commentId, VoteCommentRequest request) {
        Optional<PageCommentVoteTracking> commentVoteTrackingOptional = find(commentId, request.userId, request.ip);
        if (commentVoteTrackingOptional.isPresent()) {
            PageCommentVoteTracking commentVoteTracking = commentVoteTrackingOptional.get();
            commentVoteTracking.canceled = !commentVoteTracking.canceled;
            repository.update(commentVoteTracking.id, commentVoteTracking);
            return commentVoteTracking;
        } else {
            PageCommentVoteTracking commentVoteTracking = new PageCommentVoteTracking();
            commentVoteTracking.id = UUID.randomUUID().toString();
            commentVoteTracking.commentId = commentId;
            commentVoteTracking.userId = request.userId;
            commentVoteTracking.ip = request.ip;
            commentVoteTracking.createdBy = request.requestBy;
            commentVoteTracking.createdTime = OffsetDateTime.now();
            repository.insert(commentVoteTracking);
            return commentVoteTracking;
        }
    }

    private Optional<PageCommentVoteTracking> find(String commentId, String userId, String ip) {
        return repository.query("SELECT t FROM PageCommentVoteTracking t WHERE t.commentId=?0 AND (t.userId=?1 OR t.ip=?2)", commentId, userId, ip).findOne();
    }
}
