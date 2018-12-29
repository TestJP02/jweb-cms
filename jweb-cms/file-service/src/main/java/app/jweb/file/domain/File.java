package app.jweb.file.domain;


import app.jweb.file.api.file.FileStatus;

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
@Table(name = "file_file")
public class File {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "directory_id", length = 36)
    public String directoryId;
    @Column(name = "path", length = 128)
    public String path;
    @Column(name = "file_name", length = 64)
    public String fileName;
    @Column(name = "length")
    public Long length;
    @Column(name = "title", length = 256)
    public String title;
    @Column(name = "description", length = 512)
    public String description;
    @Column(name = "tags", length = 512)
    public String tags;
    @Column(name = "extension", length = 16)
    public String extension;
    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public FileStatus status;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by", length = 64)
    public String createdBy;
    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;
    @Column(name = "updated_by", length = 66)
    public String updatedBy;
}
