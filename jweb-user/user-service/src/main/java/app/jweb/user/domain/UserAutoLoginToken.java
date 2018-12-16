package app.jweb.user.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "user_auto_login_token")
public class UserAutoLoginToken {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "user_id", length = 36)
    public String userId;

    @Column(name = "token", length = 64)
    public String token;

    @Column(name = "expired_time")
    public OffsetDateTime expiredTime;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;
}
