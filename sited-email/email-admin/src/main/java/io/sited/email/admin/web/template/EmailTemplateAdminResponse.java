package io.sited.email.admin.web.template;

import io.sited.email.api.template.EmailTemplateStatus;

import java.time.OffsetDateTime;

/**
 * @author chi
 */
public class EmailTemplateAdminResponse {
    public String id;
    public String name;
    public String subject;
    public String language;
    public String content;
    public EmailTemplateStatus status;
    public OffsetDateTime createdTime;
    public String createdBy;
    public OffsetDateTime updatedTime;
    public String updatedBy;
}
