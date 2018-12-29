package app.jweb.user.domain;


import app.jweb.user.api.user.UserGroupStatus;

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
@Table(name = "user_group")
public class UserGroup {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "name", length = 64)
    public String name;

    @Column(name = "description", length = 512)
    public String description;

    @Column(name = "roles", length = 512)
    public String roles;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public UserGroupStatus status;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
