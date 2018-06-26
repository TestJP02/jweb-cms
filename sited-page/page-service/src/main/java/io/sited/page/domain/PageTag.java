package io.sited.page.domain;


import io.sited.page.api.tag.PageTagStatus;

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
@Table(name = "page_tag")
public class PageTag {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "name")
    public String name;
    @Column(name = "path")
    public String path;
    @Column(name = "total_tagged")
    public Integer totalTagged;
    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public PageTagStatus status;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;
    @Column(name = "created_by")
    public String createdBy;
    @Column(name = "updated_by")
    public String updatedBy;
}
