package app.jweb.post.service;

import app.jweb.database.Repository;
import app.jweb.post.api.vote.VotePostRequest;
import app.jweb.post.api.vote.VoteType;
import app.jweb.post.domain.PostVoteTracking;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PostVoteTrackingService {
    @Inject
    Repository<PostVoteTracking> repository;

    @Inject
    PostStatisticsService postStatisticsService;

    public Optional<PostVoteTracking> find(String postId, String userId, String ip) {
        return repository.query("SELECT t FROM PostVoteTracking t WHERE t.postId=?0 AND (t.userId=?1 OR t.ip=?2)", postId, userId, ip).findOne();
    }

    @Transactional
    public PostVoteTracking create(VotePostRequest request) {
        Optional<PostVoteTracking> postVoteTrackingOptional = find(request.postId, request.userId, request.ip);
        if (postVoteTrackingOptional.isPresent()) {
            return postVoteTrackingOptional.get();
        }
        int count = request.count == null ? 1 : request.count;
        PostVoteTracking postVoteTracking = new PostVoteTracking();
        postVoteTracking.id = UUID.randomUUID().toString();
        postVoteTracking.postId = request.postId;
        postVoteTracking.userId = request.userId;
        postVoteTracking.ip = request.ip;
        postVoteTracking.count = count;
        postVoteTracking.type = request.type;
        postVoteTracking.createdTime = OffsetDateTime.now();
        postVoteTracking.createdBy = request.requestBy;
        repository.insert(postVoteTracking);
        if (request.type == VoteType.LIKE) {
            postStatisticsService.like(request.postId, count, request.requestBy);
        } else {
            postStatisticsService.dislike(request.postId, count, request.requestBy);
        }
        return postVoteTracking;
    }

}
