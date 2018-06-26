package io.sited.email.domain;


import io.sited.email.api.template.EmailTemplateStatus;

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
@Table(name = "email_template")
public class EmailTemplate {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "name", length = 64)
    public String name;

    @Column(name = "subject", length = 512)
    public String subject;

    @Column(name = "content", columnDefinition = "text")
    public String content;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public EmailTemplateStatus status;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
