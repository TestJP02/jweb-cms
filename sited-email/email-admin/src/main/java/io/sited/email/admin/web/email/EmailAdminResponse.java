package io.sited.email.admin.web.email;

import io.sited.email.api.email.SendEmailStatus;

import java.time.OffsetDateTime;

/**
 * @author chi
 */
public class EmailAdminResponse {
    public String id;
    public String from;
    public String replyTo;
    public String to;
    public String subject;
    public String content;
    public SendEmailStatus status;
    public String errorMessage;
    public OffsetDateTime createdTime;
    public String createdBy;
}
