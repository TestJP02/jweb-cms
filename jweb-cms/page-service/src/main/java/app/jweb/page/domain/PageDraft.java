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
@Table(name = "page_draft")
public class PageDraft {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "draft_id", length = 36)
    public String draftId;
    @Column(name = "page_id", length = 36)
    public String pageId;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by", length = 64)
    public String createdBy;
}
