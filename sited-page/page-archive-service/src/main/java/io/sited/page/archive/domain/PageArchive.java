package io.sited.page.archive.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "page_archive", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class PageArchive {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "name", length = 8)
    public String name;
    @Column(name = "timestamp")
    public OffsetDateTime timestamp;
    @Column(name = "count")
    public Integer count;
    @Version
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by")
    public String createdBy;
    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;
    @Column(name = "updated_by")
    public String updatedBy;
}
