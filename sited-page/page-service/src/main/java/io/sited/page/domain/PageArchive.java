package io.sited.page.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "page_archive")
public class PageArchive {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "year")
    public Integer year;
    @Column(name = "month")
    public Integer month;
    @Column(name = "count")
    public Integer count;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by")
    public String createdBy;
    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;
    @Column(name = "updated_by")
    public String updatedBy;
}
