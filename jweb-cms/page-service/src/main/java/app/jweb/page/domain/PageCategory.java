package app.jweb.page.domain;


import app.jweb.page.api.category.CategoryStatus;

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
@Table(name = "page_category")
public class PageCategory {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "parent_id", length = 36)
    public String parentId;

    @Column(name = "parent_ids", length = 512)
    public String parentIds;

    @Column(name = "level")
    public Integer level;

    @Column(name = "display_name", length = 127)
    public String displayName;

    @Column(name = "keywords", length = 511)
    public String keywords;

    @Column(name = "display_order")
    public Integer displayOrder;

    @Column(name = "image_url")
    public String imageURL;

    @Column(name = "description", length = 1023)
    public String description;

    @Column(name = "tags", length = 511)
    public String tags;

    @Column(name = "fields", length = 2048)
    public String fields;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public CategoryStatus status;

    @Column(name = "owner_id")
    public String ownerId;

    @Column(name = "owner_roles")
    public String ownerRoles;

    @Column(name = "group_id")
    public String groupId;

    @Column(name = "group_roles")
    public String groupRoles;

    @Column(name = "others_roles")
    public String othersRoles;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 63)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 63)
    public String updatedBy;
}
