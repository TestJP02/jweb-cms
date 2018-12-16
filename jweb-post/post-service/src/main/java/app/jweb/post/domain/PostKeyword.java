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
@Table(name = "post_keyword")
public class PostKeyword {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "keyword")
    public String keyword;
    @Column(name = "path")
    public String path;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;
    @Column(name = "created_by")
    public String createdBy;
    @Column(name = "updated_by")
    public String updatedBy;
}
