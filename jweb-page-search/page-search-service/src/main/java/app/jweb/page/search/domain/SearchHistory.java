package app.jweb.page.search.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "search_history")
public class SearchHistory {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "category_id", length = 36)
    public String categoryId;
    @Column(name = "user_id", length = 36)
    public String userId;
    @Column(name = "keywords", length = 128)
    public String keywords;
    @Column(name = "ip", length = 32)
    public String ip;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by", length = 64)
    public String createdBy;
}
