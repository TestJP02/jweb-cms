package io.sited.comment.domain;


import io.sited.comment.api.comment.CommentStatus;

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
@Table(name = "page_comment")
public class PageComment {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "page_id", length = 36)
    public String pageId;

    @Column(name = "user_id", length = 36)
    public String userId;

    @Column(name = "parent_id", length = 36)
    public String parentId;

    @Column(name = "first_parent_id", length = 36)
    public String firstParentId;

    @Column(name = "ip")
    public String ip;

    @Column(name = "content", length = 1024)
    public String content;

    @Column(name = "total_replies")
    public Integer totalReplies;

    @Column(name = "total_vote_up")
    public Integer totalVoteUp;

    @Column(name = "total_vote_down")
    public Integer totalVoteDown;

    @Column(name = "status", length = 36)
    @Enumerated(EnumType.STRING)
    public CommentStatus status;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 63)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 63)
    public String updatedBy;
}
