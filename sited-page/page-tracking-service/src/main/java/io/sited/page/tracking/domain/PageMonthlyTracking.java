package io.sited.page.tracking.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "page_tracking_monthly_tracking")
public class PageMonthlyTracking {
    @Id
    @Column(name = "id", length = 64)
    public String id;
    @Column(name = "page_id", length = 64)
    public String pageId;
    @Column(name = "category_id", length = 64)
    public String categoryId;
    @Column(name = "month", length = 8)
    //yyyy-MM
    public String month;
    @Column(name = "total_visited")
    public Integer totalVisited;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by")
    public String createdBy;
}
