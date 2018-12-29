package app.jweb.post.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "post_content")
public class PostContent {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "post_id", length = 36)
    public String postId;

    @Column(name = "content", columnDefinition = "mediumtext")
    public String content;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
