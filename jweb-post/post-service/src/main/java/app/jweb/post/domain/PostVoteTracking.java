package app.jweb.post.domain;


import app.jweb.post.api.vote.VoteType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "post_vote_tracking")
public class PostVoteTracking {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "post_id", length = 36)
    public String postId;
    @Column(name = "user_id", length = 36)
    public String userId;
    @Column(name = "ip", length = 36)
    public String ip;
    @Column(name = "count")
    public Integer count;
    @Column(name = "type", length = 16)
    @Enumerated(EnumType.STRING)
    public VoteType type;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by", length = 64)
    public String createdBy;
}
