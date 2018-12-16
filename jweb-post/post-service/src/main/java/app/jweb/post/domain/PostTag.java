package app.jweb.post.domain;


import app.jweb.post.api.tag.PostTagStatus;

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
@Table(name = "post_tag")
public class PostTag {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "parent_id", length = 36)
    public String parentId;
    @Column(name = "parent_ids", length = 36)
    public String parentIds;
    @Column(name = "level")
    public Integer level;
    @Column(name = "name", length = 128)
    public String name;
    @Column(name = "display_name", length = 128)
    public String displayName;
    @Column(name = "alias", length = 128)
    public String alias;
    @Column(name = "tags", length = 256)
    public String tags;
    @Column(name = "fields", length = 2048)
    public String fields;
    @Column(name = "display_order")
    public Integer displayOrder;
    @Column(name = "image_url", length = 128)
    public String imageURL;
    @Column(name = "description", length = 512)
    public String description;
    @Column(name = "type", length = 32)
    public String type;
    @Column(name = "total_tagged")
    public Integer totalTagged;
    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public PostTagStatus status;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;
    @Column(name = "created_by")
    public String createdBy;
    @Column(name = "updated_by")
    public String updatedBy;
}
