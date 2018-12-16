package app.jweb.page.domain;


import app.jweb.page.api.template.TemplateStatus;
import app.jweb.page.api.template.TemplateType;

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
@Table(name = "page_template")
public class PageTemplate {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "user_id", length = 36)
    public String userId;

    @Column(name = "path", length = 128)
    public String path;

    @Column(name = "template_path", length = 128)
    public String templatePath;

    @Column(name = "title", length = 256)
    public String title;

    @Column(name = "description", length = 512)
    public String description;

    @Column(name = "tags", length = 512)
    public String tags;

    @Column(name = "type", length = 32)
    @Enumerated(EnumType.STRING)
    public TemplateType type;

    @Column(name = "sections", columnDefinition = "text")
    public String sections;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public TemplateStatus status;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
