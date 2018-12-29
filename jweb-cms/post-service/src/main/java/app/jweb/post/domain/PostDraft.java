package app.jweb.post.domain;


import app.jweb.post.api.post.PostStatus;

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
@Table(name = "post_draft")
public class PostDraft {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "post_id", length = 36)
    public String postId;

    @Column(name = "user_id", length = 36)
    public String userId;

    @Column(name = "path")
    public String path;

    @Column(name = "template_path")
    public String templatePath;

    @Column(name = "version")
    public Integer version;

    @Column(name = "keywords", length = 1024)
    public String keywords;

    @Column(name = "category_id", length = 36)
    public String categoryId;

    @Column(name = "title", length = 256)
    public String title;

    @Column(name = "description", length = 512)
    public String description;

    @Column(name = "tags", length = 512)
    public String tags;

    @Column(name = "image_url", length = 512)
    public String imageURL;

    @Column(name = "image_urls", length = 2048)
    public String imageURLs;

    @Column(name = "fields", length = 2048)
    public String fields;

    @Column(name = "top_fixed")
    public Boolean topFixed;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public PostStatus status;

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
