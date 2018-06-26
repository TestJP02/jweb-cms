package io.sited.email.domain;


import io.sited.email.api.email.SendEmailStatus;

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
@Table(name = "email_tracking")
public class EmailTracking {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "from_email", length = 128)
    public String from;

    @Column(name = "reply_to", length = 64)
    public String replyTo;

    @Column(name = "to_email", length = 128)
    public String to;

    @Column(name = "subject", length = 128)
    public String subject;

    @Column(name = "content", columnDefinition = "text")
    public String content;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public SendEmailStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    public String errorMessage;

    @Column(name = "result", length = 128)
    public String result;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;
}
