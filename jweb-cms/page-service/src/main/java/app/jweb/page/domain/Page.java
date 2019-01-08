package app.jweb.page.domain;


import app.jweb.page.api.page.PageStatus;

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
@Table(name = "page_page")
public class Page {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "user_id", length = 36)
    public String userId;

    @Column(name = "category_id", length = 36)
    public String categoryId;

    @Column(name = "category_ids", length = 360)
    public String categoryIds;

    @Column(name = "path", length = 128)
    public String path;

    @Column(name = "title", length = 256)
    public String title;

    @Column(name = "description", length = 512)
    public String description;

    @Column(name = "tags", length = 512)
    public String tags;

    @Column(name = "keywords", length = 512)
    public String keywords;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public PageStatus status;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
