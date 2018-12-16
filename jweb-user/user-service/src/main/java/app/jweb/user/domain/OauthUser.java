package app.jweb.user.domain;


import app.jweb.user.api.oauth.Provider;

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
@Table(name = "user_oauth_user")
public class OauthUser {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "user_id", length = 36)
    public String userId;
    @Column(name = "username", length = 64)
    public String username;
    @Column(name = "email", length = 128)
    public String email;
    @Column(name = "phone", length = 16)
    public String phone;
    @Column(name = "provider", length = 16)
    @Enumerated(EnumType.STRING)
    public Provider provider;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by", length = 64)
    public String createdBy;
    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;
    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
