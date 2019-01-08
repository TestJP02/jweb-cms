package app.jweb.page.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "page_template")
public class PageTemplate {
    @Id
    @Column(name = "page_id", length = 36)
    public String pageId;

    @Column(name = "sections", columnDefinition = "mediumtext")
    public String sections;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
