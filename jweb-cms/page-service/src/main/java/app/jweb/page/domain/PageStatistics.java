package app.jweb.page.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "page_statistics")
public class PageStatistics {
    @Id
    @Column(name = "id", length = 36)
    public String id;
    @Column(name = "category_id", length = 36)
    public String categoryId;
    @Column(name = "total_visited")
    public Integer totalVisited;
    @Column(name = "total_daily_visited")
    public Integer totalDailyVisited;
    @Column(name = "total_weekly_visited")
    public Integer totalWeeklyVisited;
    @Column(name = "total_monthly_visited")
    public Integer totalMonthlyVisited;
    @Column(name = "total_commented")
    public Integer totalCommented;
    @Column(name = "total_liked")
    public Integer totalLiked;
    @Column(name = "total_daily_liked")
    public Integer totalDailyLiked;
    @Column(name = "total_weekly_liked")
    public Integer totalWeeklyLiked;
    @Column(name = "total_monthly_liked")
    public Integer totalMonthlyLiked;
    @Column(name = "total_disliked")
    public Integer totalDisliked;
    @Column(name = "created_time")
    public OffsetDateTime createdTime;
    @Column(name = "created_by", length = 64)
    public String createdBy;
    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;
    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
