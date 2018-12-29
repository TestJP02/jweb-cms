package app.jweb.page.rating.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "page_rating_tracking")
public class PageRatingTracking {
    @Id
    @Column(name = "id")
    public String id;
    @Column(name = "page_id")
    public String pageId;
    @Column(name = "user_id")
    public String userId;
    @Column(name = "ip")
    public String ip;
    @Column(name = "score")
    public Integer score;
    @Column(name = "content")
    public String content;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by")
    public String createdBy;
}
