package io.sited.user.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "user_reset_password_token")
public class ResetPasswordToken {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "user_id", length = 36)
    public String userId;

    @Column(name = "token", length = 64)
    public String token;

    @Column(name = "removed")
    @Deprecated
    public Boolean removed;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;
}
