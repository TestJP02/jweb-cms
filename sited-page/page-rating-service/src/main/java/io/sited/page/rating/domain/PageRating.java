package io.sited.page.rating.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "page_rating_rating")
public class PageRating {
    @Id
    @Column(name = "id")
    public String id;
    @Column(name = "page_id")
    public String pageId;
    @Column(name = "total_score")
    public Integer totalScore;
    @Column(name = "total_scored")
    public Integer totalScored;
    @Column(name = "total_scored1")
    public Integer totalScored1;
    @Column(name = "total_scored2")
    public Integer totalScored2;
    @Column(name = "total_scored3")
    public Integer totalScored3;
    @Column(name = "total_scored4")
    public Integer totalScored4;
    @Column(name = "total_scored5")
    public Integer totalScored5;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by")
    public String createdBy;
    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;
    @Column(name = "updated_by")
    public String updatedBy;
}
