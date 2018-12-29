package app.jweb.file.domain;


import app.jweb.file.api.directory.DirectoryStatus;

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
@Table(name = "file_directory")
public class Directory {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "parent_id", length = 36)
    public String parentId;
    @Column(name = "parent_ids", length = 512)
    public String parentIds;
    @Column(name = "path", length = 128)
    public String path;
    @Column(name = "name", length = 64)
    public String name;
    @Column(name = "description", length = 512)
    public String description;
    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public DirectoryStatus status;

    @Column(name = "owner_id", length = 36)
    public String ownerId;

    @Column(name = "owner_roles", length = 512)
    public String ownerRoles;

    @Column(name = "group_id", length = 36)
    public String groupId;

    @Column(name = "group_roles", length = 512)
    public String groupRoles;

    @Column(name = "others_roles", length = 512)
    public String othersRoles;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
