package io.sited.user.domain;


import io.sited.user.api.user.Gender;
import io.sited.user.api.user.UserStatus;

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
@Table(name = "user_user")
public class User {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "username", length = 64)
    public String username;

    @Column(name = "nickname", length = 64)
    public String nickname;

    @Column(name = "phone", length = 16)
    public String phone;

    @Column(name = "email", length = 128)
    public String email;

    @Column(name = "image_url", length = 512)
    public String imageURL;

    @Column(name = "user_group_ids", length = 512)
    public String userGroupIds;

    @Column(name = "salt", length = 64)
    public String salt;

    @Column(name = "iteration")
    public Integer iteration;

    @Column(name = "hashed_password", length = 32)
    public String hashedPassword;

    @Deprecated
    @Column(name = "type")
    public String type;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public UserStatus status;

    @Column(name = "description", length = 512)
    public String description;

    @Column(name = "language", length = 16)
    public String language;

    @Column(name = "gender", length = 16)
    @Enumerated(EnumType.STRING)
    public Gender gender;

    @Column(name = "country", length = 16)
    public String country;

    @Column(name = "state", length = 64)
    public String state;

    @Column(name = "city", length = 64)
    public String city;

    @Column(name = "channel", length = 32)
    public String channel;

    @Column(name = "campaign", length = 32)
    public String campaign;

    @Column(name = "tags", length = 512)
    public String tags;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;

    @Column(name = "fields", length = 2048)
    public String fields;
}
