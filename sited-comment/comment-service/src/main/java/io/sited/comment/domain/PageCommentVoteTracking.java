package io.sited.comment.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "page_comment_vote_tracking")
public class PageCommentVoteTracking {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "comment_id", length = 36)
    public String commentId;

    @Column(name = "user_id", length = 36)
    public String userId;

    @Column(name = "ip", length = 36)
    public String ip;

    @Column(name = "canceled")
    public Boolean canceled;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 63)
    public String createdBy;
}
